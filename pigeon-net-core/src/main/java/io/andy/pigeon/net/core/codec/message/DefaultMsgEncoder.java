package io.andy.pigeon.net.core.codec.message;

import io.andy.pigeon.net.core.message.MsgEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class DefaultMsgEncoder implements MsgEncoder<MsgEnvelope> {

    @Override
    public void encode(ChannelHandlerContext ctx, MsgEnvelope msg, ByteBuf out) throws Exception {
        // 基本参数信息
        out.writeByte(msg.getMagic());
        out.writeByte(msg.getType());
        out.writeByte(msg.getCodec());
        out.writeLong(msg.getReqId());

        // 判空长度写0
        if (msg.getBody() == null) {
            out.writeShort(0);
            out.writeInt(0);
            return;
        }

        out.writeShort(msg.getClazz().length);
        out.writeInt(msg.getBody().length);
        out.writeBytes(msg.getClazz());
        out.writeBytes(msg.getBody());
    }
}
