package io.andy.pigeon.net.core.message.processor;

import io.andy.pigeon.net.core.message.*;
import io.andy.pigeon.net.core.utils.ProtoStuffUtils;
import org.springframework.beans.BeanUtils;

public interface MsgProcessor {

    void process(MsgContext context, Envelope req);

    default ReqMsg deserializeReqBody(Envelope req) throws Throwable {
        MsgEnvelope envelope = (MsgEnvelope) req;
        ReqMsg reqMsg = new ReqMsg(null);
        BeanUtils.copyProperties(envelope ,reqMsg);

        if (envelope.getBodyLength() <= 0 || envelope.getBody() == null) {
            return reqMsg;
        }

        reqMsg.setRequest(ProtoStuffUtils.deserialize(envelope.getBody(), getClazzType(req)));

        return reqMsg;
    }

    default RespMsg deserializeRespBody(Envelope req) throws Throwable {
        MsgEnvelope envelope = (MsgEnvelope) req;
        RespMsg respMsg = new RespMsg(null);
        BeanUtils.copyProperties(envelope , respMsg);

        if (envelope.getBodyLength() <= 0 || envelope.getBody() == null) {
            return respMsg;
        }

        respMsg.setResponse(ProtoStuffUtils.deserialize(respMsg.getBody(), getClazzType(req)));

        return respMsg;
    }

    static Class<?> getClazzType(Envelope req) throws Throwable {
        Class<?> classType;
        try {
            classType = Class.forName(req.getClazzStr());
        } catch (ClassNotFoundException e) {
            throw new Throwable("Deserialize message body failed!");
        }
        return classType;
    }

}
