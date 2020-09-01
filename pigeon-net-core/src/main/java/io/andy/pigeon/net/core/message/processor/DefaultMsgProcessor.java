package io.andy.pigeon.net.core.message.processor;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.ReqMsg;
import io.andy.pigeon.net.core.message.RespMsg;
import io.andy.pigeon.net.core.message.invoker.DefaultInvokerMgr;
import io.andy.pigeon.net.core.message.invoker.InvokeFuture;
import io.andy.pigeon.net.core.message.invoker.Invoker;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DefaultMsgProcessor implements MsgProcessor {
    private Invoker oneWayInvoker;
    private Invoker twoWayInvoker;

    public DefaultMsgProcessor() {
        this.oneWayInvoker = DefaultInvokerMgr.getInstance().getOneWayInvoker();
        this.twoWayInvoker = DefaultInvokerMgr.getInstance().getTwoWayInvoker();
    }

    @Override
    public void process(final ChannelHandlerContext ctx, Envelope message) {

        switch (message.getMsgType()) {
            case REQ_ONE_WAY:
                processOneWayReq(message);
                break;
            case REQ_TWO_WAY:
                processTwoWayReq(ctx, message);
                break;
            case ACK_TWO_WAY:
                processTwoWayResp(ctx, message);
                break;
            case REQ_HEARTBEAT:
                processHeartbeatReq(ctx, message);
                break;
            case ACK_HEARTBEAT:
                log.info("Received heartbeat ack message={}, remoteAddr={}",
                        message, RemotingUtil.parseRemoteAddress(ctx.channel()));
                break;
        }
    }

    private void processOneWayReq(Envelope req) {
        ReqMsg reqMsg = deserializeReqMsg(req);
        oneWayInvoker.invoke(reqMsg);
    }

    private void processTwoWayReq(final ChannelHandlerContext ctx, Envelope req) {
        ReqMsg reqMsg = deserializeReqMsg(req);
        RespMsg respMsg = (RespMsg) twoWayInvoker.invoke(reqMsg);

        ctx.writeAndFlush(respMsg).addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("Send two way ack done! , to remoteAddr={}",
                            RemotingUtil.parseRemoteAddress(ctx.channel()));
                }
            } else {
                log.error("Send two way ack failed! Id={}, to remoteAddr={}",
                        RemotingUtil.parseRemoteAddress(ctx.channel()));
            }
        });
    }

    private void processTwoWayResp(final ChannelHandlerContext ctx, Envelope req) {
        RespMsg respMsg = deserializeRespMsg(req);
        Connection conn = ctx.channel().attr(Connection.CONNECTION).get();
        InvokeFuture future = conn.removeInvokeFuture(req.getReqId());

        if (future != null) {
            future.complete(respMsg);
        } else {
            log.warn("Can't find InvokeFuture, maybe already timeout, id={}, from={} ",
                    req.getReqId(), RemotingUtil.parseRemoteAddress(ctx.channel()));
        }
    }

    private void processHeartbeatReq(final ChannelHandlerContext ctx, Envelope req) {
        Envelope heartbeatAck= DefaultMsgFactory.getInstance().createHeartbeatAck(req);
        final long id = req.getReqId();
        ctx.writeAndFlush(heartbeatAck).addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("Send heartbeat ack done! Id={}, to remoteAddr={}", id,
                            RemotingUtil.parseRemoteAddress(ctx.channel()));
                }
            } else {
                log.error("Send heartbeat ack failed! Id={}, to remoteAddr={}", id,
                        RemotingUtil.parseRemoteAddress(ctx.channel()));
            }
        });
    }

}
