package io.andy.pigeon.net.core;

import io.andy.pigeon.net.core.config.ClientOption;
import io.andy.pigeon.net.core.config.Option;
import io.andy.pigeon.net.core.config.Options;
import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionMgr;
import io.andy.pigeon.net.core.exception.StartException;
import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.MsgFactory;
import io.andy.pigeon.net.core.message.RespMsg;
import io.andy.pigeon.net.core.message.invoker.DefaultInvokeFuture;
import io.andy.pigeon.net.core.message.invoker.InvokeFuture;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import io.andy.pigeon.net.core.utils.TimerHolder;
import io.netty.channel.ChannelFuture;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractRemotingEndpoint implements RemotingEndpoint {

    private AtomicBoolean start = new AtomicBoolean(false);
    protected final Options options = new Options();
    protected ConnectionMgr connectionMgr;
    private MsgFactory msgFactory;

    public AbstractRemotingEndpoint() {
        this.msgFactory = DefaultMsgFactory.getInstance();
    }

    @Override
    public RemotingEndpoint start() {
        if (start.compareAndSet(false, true)) {
            return this;
        }
        throw new StartException("The node has started");
    }

    @Override
    public boolean started() {
        return start.get();
    }

    @Override
    public void stop() {
        if (start.compareAndSet(true, false)) {
            return;
        }
        throw new StartException("The node has shutdown");
    }

    @Override
    public <T> RemotingEndpoint config(Option<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        this.options.option(option, value);
        return this;
    }

    @Override
    public <T> T putOptionGet(Option<T> option) {
        return options.option(option);
    }

    protected void sendOneWayMsg(Connection conn, Object request) {
        Envelope envelope = msgFactory.createOneWay(request);
        try {
            conn.getChannel().writeAndFlush(envelope).addListener((ChannelFuture f) -> {
                if (!f.isSuccess()) {
                    log.error("One way send failed. The address is {}",
                            RemotingUtil.parseRemoteAddress(conn.getChannel()), f.cause());
                } else {
                    log.info("One way send success!!");
                }
            });
        } catch (Exception e) {
            if (null == conn) {
                log.error("Conn is null");
            } else {
                log.error("Exception caught when sending invocation. The address is {}",
                        RemotingUtil.parseRemoteAddress(conn.getChannel()), e);
            }
        }
    }


    public void sendOneWayMsg(Url url, Object request) {
        Connection conn = connectionMgr.get(url);
        sendOneWayMsg(conn, request);
    }

    public RespMsg sendSyncMsg(final Connection conn, final Object request)  throws InterruptedException{
        Envelope envelope = msgFactory.createTwoWay(request);
        final long requestId = envelope.getReqId();
        final InvokeFuture future = new DefaultInvokeFuture(requestId);
        conn.addInvokeFuture(future);

        try {
            conn.getChannel().writeAndFlush(envelope).addListener((ChannelFuture f) -> {
                if (!f.isSuccess()) {
                    conn.removeInvokeFuture(requestId);
                    future.complete(msgFactory.createReqFailed(envelope, f.cause()));

                    log.error("Two way send failed. The address is {}",
                            RemotingUtil.parseRemoteAddress(conn.getChannel()), f.cause());
                } else {
                    log.debug("Two way send success!!");
                }
            });
        } catch (Exception e) {
            conn.removeInvokeFuture(requestId);
            future.complete(msgFactory.createReqFailed(envelope, e));

            log.error("Exception caught when sending invocation. The address is {}",
                    RemotingUtil.parseRemoteAddress(conn.getChannel()), e);

        }

        RespMsg respMsg = future.get(putOptionGet(ClientOption.REQUEST_TIMEOUT));
        if (respMsg == null) {
            conn.removeInvokeFuture(requestId);
            respMsg = msgFactory.createReqTimeout(envelope, new Throwable("Sync message timeout"));
        }

        return respMsg;
    }

    protected RespMsg sendSyncMsg(Url url, Object request)  throws InterruptedException{
        Connection conn = connectionMgr.get(url);
        return sendSyncMsg(conn, request);
    }

    protected InvokeFuture sendAsyncMsg(Url url, Object request, int timeoutMillis)  throws InterruptedException{
        Connection conn = connectionMgr.get(url);
        return sendAsyncMsg(conn, request, timeoutMillis);
    }


    public InvokeFuture sendAsyncMsg(Connection conn, Object request, int timeoutMillis)  throws InterruptedException{
        Envelope envelope = msgFactory.createTwoWay(request);
        final long requestId = envelope.getReqId();
        final InvokeFuture future = new DefaultInvokeFuture(requestId);
        conn.addInvokeFuture(future);

        Timeout timeout = TimerHolder.getTimer().newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                InvokeFuture future = conn.removeInvokeFuture(requestId);
                if (future != null) {
                    future.complete(msgFactory.createReqTimeout(envelope, new Throwable("Async message timeout")));
                }
            }

        }, timeoutMillis, TimeUnit.MILLISECONDS);
        future.addTimeout(timeout);

        try {
            conn.getChannel().writeAndFlush(envelope).addListener((ChannelFuture f) -> {
                if (!f.isSuccess()) {
                    InvokeFuture invokeFuture = conn.removeInvokeFuture(requestId);
                    if (invokeFuture != null) {
                        invokeFuture.cancelTimeout();
                        invokeFuture.complete(msgFactory.createReqFailed(envelope, f.cause()));
                    }

                    log.error("Two way async send failed. The address is {}",
                            RemotingUtil.parseRemoteAddress(conn.getChannel()), f.cause());
                } else {
                    log.debug("Two way async send success!!");
                }
            });
        } catch (Exception e) {
            conn.removeInvokeFuture(requestId);
            future.complete(msgFactory.createReqFailed(envelope, e));

            log.error("Exception caught when sending invocation. The address is {}",
                    RemotingUtil.parseRemoteAddress(conn.getChannel()), e);

        }

        return future;
    }

}
