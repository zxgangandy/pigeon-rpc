package io.andy.pigeon.net.core.message;

import io.andy.pigeon.net.core.codec.message.CodecType;
import io.andy.pigeon.net.core.utils.IDGenerator;

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

        envelope.setType(MsgType.REQ_ONE_WAY.getType())
                .setCodec(CodecType.PROTO_STUFF.getType())
                .setReqId(IDGenerator.nextId())
                .setClazz(requestObject.getClass().getName().getBytes())
                .serialize(requestObject);

        return envelope;
    }

    @Override
    public Envelope createTwoWay(Object requestObject) {
        return null;
    }

    @Override
    public Envelope createHeartbeatReq() {
        MsgEnvelope  envelope =  new MsgEnvelope();

        envelope.setType(MsgType.REQ_HEARTBEAT.getType())
                .setCodec(CodecType.PROTO_STUFF.getType())
                .setReqId(IDGenerator.nextId());

        return envelope;
    }

    @Override
    public Envelope createHeartbeatAck(Envelope req) {
        MsgEnvelope  envelope =  new MsgEnvelope();

        envelope.setType(MsgType.ACK_HEARTBEAT.getType())
                .setCodec(CodecType.PROTO_STUFF.getType())
                .setReqId(req.getReqId());

        return envelope;
    }

    @Override
    public Envelope createTwoWayAck(Envelope req, Object responseBody) {
        RespMsg  envelope =  new RespMsg(responseBody);

        envelope.setType(MsgType.ACK_HEARTBEAT.getType())
                .setCodec(CodecType.PROTO_STUFF.getType())
                .setReqId(req.getReqId());

        return envelope;
    }


}
