package io.andy.pigeon.net.core.message.processor;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.message.*;
import io.andy.pigeon.net.core.message.invoker.*;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static io.andy.pigeon.net.core.message.MsgType.REQ_TWO_WAY;


@Slf4j
public class DefaultMsgProcessor implements MsgProcessor {
    private Invoker invoker;

    private MsgFactory msgFactory;

    public DefaultMsgProcessor() {
        this.invoker = DefaultInvokerMgr.getInstance().getInvoker();

        Objects.requireNonNull(invoker, "Invoker not be initialized");

        this.msgFactory = DefaultMsgFactory.getInstance();
    }

    @Override
    public void process(MsgContext context, Envelope message) {
        Connection connection = context.getConnection();

        switch (message.getMsgType()) {
            case REQ_ONE_WAY:
            case REQ_TWO_WAY:
                processReqMessage(context, message);
                break;
            case ACK_TWO_WAY:
                processTwoWayAck(connection, message);
                break;
            case REQ_HEARTBEAT:
                processHeartbeatReq(connection, message);
                break;
            case ACK_HEARTBEAT:
                log.info("Received heartbeat ack message={}, remoteAddr={}",
                        message, RemotingUtil.parseRemoteAddress(connection.getChannel()));
                break;
        }
    }


    /**
     *  process request message（including one,two way request）
     */
    private void processReqMessage(MsgContext context, Envelope message) {
        ReqMsg reqMsg;
        Connection connection = context.getConnection();
        try {
            reqMsg = deserializeReqMsg(message);
        } catch (Throwable th) {
            sendAckIfNecessary(connection, message, th);
            return;
        }

        try {
            Object respObj = invoker.invoke(context, reqMsg);
            sendAckIfNecessary(connection, message, respObj);
        } catch (Throwable th) {
            sendAckIfNecessary(connection, message, th);
        }
    }

    private void processTwoWayAck(Connection connection, Envelope req) {
        RespMsg respMsg;
        try {
            respMsg = deserializeRespMsg(req);
        } catch (Throwable e) {
            e.printStackTrace();
            respMsg = msgFactory.createTwoWayAck(req, e);
        }

        InvokeFuture future = connection.removeInvokeFuture(req.getReqId());
        if (future != null) {
            future.complete(respMsg);
            future.cancelTimeout();
        } else {
            log.warn("Can't find InvokeFuture, maybe already timeout, id={}, from={} ",
                    req.getReqId(), RemotingUtil.parseRemoteAddress(connection.getChannel()));
        }
    }

    private void processHeartbeatReq(Connection connection, Envelope req) {
        Envelope heartbeatAck= msgFactory.createHeartbeatAck(req);
        connection.sendMsg(heartbeatAck);
    }

    private void sendAckIfNecessary(Connection connection, Envelope message, Object respObj) {
        if (message.getMsgType() == REQ_TWO_WAY && respObj != null) {
            RespMsg respMsg = msgFactory.createTwoWayAck(message, respObj);
            connection.sendMsg(respMsg);
        }
    }

}
