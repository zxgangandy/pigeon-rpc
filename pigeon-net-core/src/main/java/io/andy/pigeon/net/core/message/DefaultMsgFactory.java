package io.andy.pigeon.net.core.message;

import io.andy.pigeon.net.core.serialization.CodecType;
import io.andy.pigeon.net.core.utils.IDGenerator;

public class DefaultMsgFactory implements MsgFactory {

    @Override
    public Envelope createOneWay(Object requestObject) {
        ReqMsg  reqMsg =  new ReqMsg(requestObject);

        reqMsg.setType(MsgType.REQ_ONE_WAY.getType())
                .setCodec(CodecType.CODEC_TYPE_PROTO_STUFF.getType())
                .setReqId(IDGenerator.nextId())
                .serialize(reqMsg.getRequest());

        return reqMsg;
    }

    @Override
    public Envelope createTwoWay(Object requestObject) {
        return null;
    }
}
