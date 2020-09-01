package io.andy.pigeon.net.core.codec.message;

import io.andy.pigeon.net.core.message.MsgEnvelope;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class DefaultMsgDecoder implements MsgDecoder{

    private int MIN_ENVELOPE_BYTES = envelopSize(0);

    public int envelopSize(int extraLength) {
        return getMsgHeaderSize() + extraLength;
    }

    private MsgHeaderDecoder headerDecoder;

    public DefaultMsgDecoder(MsgHeaderDecoder headerDecoder) {
        this.headerDecoder = headerDecoder;
    }

    @Override
    public void decode(ByteBuf in, List<Object> out) {
        decodeByteBuf(in, out);
    }

    private void decodeByteBuf(ByteBuf in, List<Object> out) {
        int readable = in.readableBytes();
        int saveReadIndex = in.readerIndex();

        if (readable < MIN_ENVELOPE_BYTES) {
            in.readerIndex(saveReadIndex);
            return;
        }

        byte[] header = new byte[MIN_ENVELOPE_BYTES];
        in.readBytes(header);
        byte magic = headerDecoder.getMagic(header);

        if (!isMagicValid(magic)) {
            return;
        }

        short clazzLength = headerDecoder.getClazzLength(header);
        int bodyLength = headerDecoder.getBodyLength(header);
        int extraLength = clazzLength + bodyLength;

        if( readable >= envelopSize(extraLength)) {
            out.add(decodeMsg(in, header, magic, clazzLength, bodyLength));
        } else {
            in.readerIndex(saveReadIndex);
        }
    }

    private MsgEnvelope decodeMsg(ByteBuf in, byte[] header, byte magic, short clazzLength, int bodyLength) {
        MsgEnvelope msg = new MsgEnvelope();
        msg.setMagic(magic);
        msg.setType(headerDecoder.getType(header));
        msg.setCodec(headerDecoder.getCodec(header));
        msg.setReqId(headerDecoder.getReqId(header));
        msg.setBodyLength(bodyLength);
        msg.setClazzLength(clazzLength);

        setClazzAndBodyIfNecessary(in, msg);

        return msg;
    }

    private void setClazzAndBodyIfNecessary(ByteBuf in, MsgEnvelope msg) {
        if (msg.getBodyLength() > 0) {
            byte[] clazz = new byte[msg.getClazzLength()];
            in.readBytes(clazz);
            msg.setClazz(clazz);

            byte[] body = new byte[msg.getBodyLength()];
            in.readBytes(body);
            msg.setBody(body);
        }
    }

    private boolean isMagicValid(byte magicValue)  {
        return magicValue == MAGIC;
    }

}
