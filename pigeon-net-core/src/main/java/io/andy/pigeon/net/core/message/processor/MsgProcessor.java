package io.andy.pigeon.net.core.message.processor;

import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.MsgEnvelope;
import io.andy.pigeon.net.core.message.ReqMsg;
import io.andy.pigeon.net.core.message.RespMsg;
import io.andy.pigeon.net.core.utils.ProtoStuffUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;

public interface MsgProcessor {

    void process(final ChannelHandlerContext ctx, Envelope req);

    default ReqMsg deserializeReqMsg(Envelope req)  {
        MsgEnvelope envelope = (MsgEnvelope) req;
        ReqMsg reqMsg = new ReqMsg(null);
        BeanUtils.copyProperties(envelope ,reqMsg);

        if (envelope.getBodyLength() <= 0 || envelope.getBody() == null) {
            return reqMsg;
        }

        reqMsg.setRequest(ProtoStuffUtils.deserialize(envelope.getBody(), getClazzType(req)));

        return reqMsg;
    }

    default RespMsg deserializeRespMsg(Envelope req)  {
        MsgEnvelope envelope = (MsgEnvelope) req;
        RespMsg respMsg = new RespMsg(null);
        BeanUtils.copyProperties(envelope , respMsg);

        if (envelope.getBodyLength() <= 0 || envelope.getBody() == null) {
            return respMsg;
        }

        respMsg.setResponse(ProtoStuffUtils.deserialize(respMsg.getBody(), getClazzType(req)));

        return respMsg;
    }

    static Class<?> getClazzType(Envelope req) {
        Class<?> classType = null;
        try {
            classType = Class.forName(req.getClazzStr());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classType;
    }

}
