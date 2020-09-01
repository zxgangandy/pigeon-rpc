package io.andy.pigeon.net.core.message;

import io.andy.pigeon.net.core.utils.IDGenerator;

import static io.andy.pigeon.net.core.codec.message.CodecType.PROTO_STUFF;
import static io.andy.pigeon.net.core.message.MsgType.*;

public class DefaultMsgFactory implements MsgFactory {
    private static volatile DefaultMsgFactory INSTANCE;

    public static DefaultMsgFactory getInstance() {
        if (null == INSTANCE) {
            synchronized (DefaultMsgFactory.class) {
                if (null == INSTANCE) {
                    INSTANCE = new DefaultMsgFactory();
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public Envelope createOneWay(Object requestObject) {
        MsgEnvelope  envelope =  new MsgEnvelope();

        envelope.setType(REQ_ONE_WAY.getType())
                .setCodec(PROTO_STUFF.getType())
                .setReqId(IDGenerator.nextId())
                .setClazz(requestObject.getClass().getName().getBytes())
                .serialize(requestObject);

        return envelope;
    }

    @Override
    public Envelope createTwoWay(Object requestObject) {
        MsgEnvelope  envelope =  new MsgEnvelope();

        envelope.setType(REQ_TWO_WAY.getType())
                .setCodec(PROTO_STUFF.getType())
                .setReqId(IDGenerator.nextId())
                .setClazz(requestObject.getClass().getName().getBytes())
                .serialize(requestObject);

        return envelope;
    }

    @Override
    public Envelope createHeartbeatReq() {
        MsgEnvelope  envelope =  new MsgEnvelope();

        envelope.setType(REQ_HEARTBEAT.getType())
                .setCodec(PROTO_STUFF.getType())
                .setReqId(IDGenerator.nextId());

        return envelope;
    }

    @Override
    public Envelope createHeartbeatAck(Envelope req) {
        MsgEnvelope  envelope =  new MsgEnvelope();

        envelope.setType(ACK_HEARTBEAT.getType())
                .setCodec(PROTO_STUFF.getType())
                .setReqId(req.getReqId());

        return envelope;
    }

    @Override
    public RespMsg createTwoWayAck(Envelope req, Object responseBody) {
        RespMsg  envelope =  new RespMsg(responseBody);

        envelope.setType(ACK_TWO_WAY.getType())
                .setCodec(PROTO_STUFF.getType())
                .setClazz(responseBody.getClass().getName().getBytes())
                .setReqId(req.getReqId())
                .serialize(responseBody);

        return envelope;
    }

    @Override
    public RespMsg createReqTimeout(Envelope req, Object responseBody) {
        RespMsg  envelope =  new RespMsg(responseBody);

        envelope.setType(ACK_TWO_WAY.getType())
                .setCodec(PROTO_STUFF.getType())
                .setReqId(req.getReqId());

        return envelope;
    }

    @Override
    public RespMsg createReqFailed(Envelope req, Object responseBody) {
        RespMsg  envelope =  new RespMsg(responseBody);

        envelope.setType(ACK_TWO_WAY.getType())
                .setCodec(PROTO_STUFF.getType())
                .setReqId(req.getReqId());

        return envelope;
    }


}
