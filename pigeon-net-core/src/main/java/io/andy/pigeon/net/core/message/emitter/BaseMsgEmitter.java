package io.andy.pigeon.net.core.message.emitter;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionMgr;
import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.MsgEnvelope;
import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

@Slf4j
public abstract class BaseMsgEmitter {
    protected ConnectionMgr connectionMgr;

    public BaseMsgEmitter(ConnectionMgr connectionMgr) {
        this.connectionMgr = connectionMgr;
    }

    public MsgEnvelope invokeSync(final Url url, final Object request) {
        Connection conn = this.connectionMgr.get(url);
        return invokeSync(conn, request);
    }

    public void oneWay(final Connection conn, final Object request) {
        Envelope envelope = DefaultMsgFactory.getInstance().createOneWay(request);
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

    public abstract void oneWay(final Url url, final Object request);

    public MsgEnvelope invokeSync(Connection conn, final Object request) {
        Envelope envelope = new DefaultMsgFactory().createTwoWay(request);
        final long requestId = envelope.getReqId();
        try {
            conn.getChannel().writeAndFlush(envelope).addListener((ChannelFuture f) -> {
                if (!f.isSuccess()) {
//                        conn.removeInvokeFuture(requestId);
//                        future.putResponse(commandFactory.createSendFailedResponse(
//                                conn.getRemoteAddress(), f.cause()));
                    log.error("Invoke send failed, id={}", requestId, f.cause());
                }
            });
        } catch (Exception e) {
            //conn.removeInvokeFuture(requestId);
//            future.putResponse(commandFactory.createSendFailedResponse(conn.getRemoteAddress(), e));
            log.error("Exception caught when sending invocation, id={}", requestId, e);
        }

        return null;
    }

    public Future invokeAsync(final Connection conn, final MsgEnvelope request, final int timeoutMillis) {
        return null;
    }

    public Future invokeAsync(final Url url, final MsgEnvelope request, final int timeoutMillis) {
        return null;
    }
}
