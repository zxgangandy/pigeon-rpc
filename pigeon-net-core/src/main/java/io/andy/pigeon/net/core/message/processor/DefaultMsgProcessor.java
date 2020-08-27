package io.andy.pigeon.net.core.message.processor;

import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.ReqMsg;
import io.andy.pigeon.net.core.message.RespMsg;
import io.andy.pigeon.net.core.message.invoker.DefaultInvokerMgr;
import io.andy.pigeon.net.core.message.invoker.Invoker;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DefaultMsgProcessor implements MsgProcessor {
    private Invoker reqInvoker;
    private Invoker respInvoker;

    public DefaultMsgProcessor() {
        this.reqInvoker = DefaultInvokerMgr.getInstance().getReqInvoker();
        this.respInvoker = DefaultInvokerMgr.getInstance().getRespInvoker();
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
                processTwoWayResp(message);
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
        reqInvoker.invoke(reqMsg);
    }

    private void processTwoWayReq(final ChannelHandlerContext ctx, Envelope req) {
        ReqMsg reqMsg = deserializeReqMsg(req);
        RespMsg respMsg = (RespMsg) reqInvoker.invoke(reqMsg);

        ctx.writeAndFlush(respMsg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Send two way ack done! , to remoteAddr={}",
                                RemotingUtil.parseRemoteAddress(ctx.channel()));
                    }
                } else {
                    log.error("Send two way ack failed! Id={}, to remoteAddr={}",
                            RemotingUtil.parseRemoteAddress(ctx.channel()));
                }
            }
        });
    }

    private void processTwoWayResp(Envelope req) {
        RespMsg respMsg = deserializeRespMsg(req);
        respInvoker.invoke(respMsg);
    }


    private void processHeartbeatReq(final ChannelHandlerContext ctx, Envelope req) {
        Envelope heartbeatAck= DefaultMsgFactory.getInstance().createHeartbeatAck(req);
        final long id = req.getReqId();
        ctx.writeAndFlush(heartbeatAck).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Send heartbeat ack done! Id={}, to remoteAddr={}", id,
                                RemotingUtil.parseRemoteAddress(ctx.channel()));
                    }
                } else {
                    log.error("Send heartbeat ack failed! Id={}, to remoteAddr={}", id,
                            RemotingUtil.parseRemoteAddress(ctx.channel()));
                }
            }
        });
    }

}
