package io.andy.pigeon.net.core.codec.message;

import io.andy.pigeon.net.core.message.MsgEnvelope;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class DefaultMsgDecoder implements MsgDecoder{

    private int MIN_ENVELOPE_BYTES = envelopSize(0);

    private int envelopSize(int extraLength) {
        return getMsgHeaderSize() + extraLength;
    }

    private MsgHeaderDecoder headerDecoder;

    public DefaultMsgDecoder(MsgHeaderDecoder headerDecoder) {
        this.headerDecoder = headerDecoder;
    }

    @Override
    public void decode(ByteBuf in, List<Object> out) {
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

    /**
     *  decode message envelop(header and extra: clazz, body)
     */
    private MsgEnvelope decodeMsg(ByteBuf in, byte[] header, byte magic, short clazzLength, int bodyLength) {
        MsgEnvelope msg = new MsgEnvelope();
        msg.setMagic(magic);
        msg.setType(headerDecoder.getType(header));
        msg.setCodec(headerDecoder.getCodec(header));
        msg.setReqId(headerDecoder.getReqId(header));
        msg.setBodyLength(bodyLength);
        msg.setClazzLength(clazzLength);

        if (msg.getBodyLength() <= 0) {
            return msg;
        }

        decodeExtraIfNecessary(in, msg);

        return msg;
    }

    private static void decodeExtraIfNecessary(ByteBuf in, MsgEnvelope msg) {
        byte[] clazz = new byte[msg.getClazzLength()];
        in.readBytes(clazz);
        msg.setClazz(clazz);

        byte[] body = new byte[msg.getBodyLength()];
        in.readBytes(body);
        msg.setBody(body);
    }

    private static boolean isMagicValid(byte magicValue)  {
        return magicValue == MAGIC;
    }

}
