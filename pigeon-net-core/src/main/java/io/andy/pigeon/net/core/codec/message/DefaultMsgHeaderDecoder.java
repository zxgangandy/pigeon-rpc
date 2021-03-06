package io.andy.pigeon.net.core.codec.message;

import static io.andy.pigeon.net.core.utils.BytesUtil.*;

public class DefaultMsgHeaderDecoder implements MsgHeaderDecoder {
    private int REQ_ID_LENGTH_OFFSET = MAGIC_VALUE_SIZE
            + TYPE_VALUE_SIZE
            + CODEC_VALUE_SIZE;

    private int CLAZZ_LENGTH_OFFSET = MAGIC_VALUE_SIZE
            + TYPE_VALUE_SIZE
            + CODEC_VALUE_SIZE
            + REQ_ID_VALUE_SIZE;

    private int BODY_LENGTH_OFFSET = MAGIC_VALUE_SIZE
            + TYPE_VALUE_SIZE
            + CODEC_VALUE_SIZE
            + REQ_ID_VALUE_SIZE
            + CLAZZ_LENGTH_SIZE;

    @Override
    public byte getMagic(byte[] header) {
        return header[0];
    }

    @Override
    public byte getType(byte[] header) {
        return header[1];
    }

    @Override
    public byte getCodec(byte[] header) {
        return header[2];
    }

    @Override
    public short getClazzLength(byte[] header) {
        short clazzLength = bytes2short(header, CLAZZ_LENGTH_OFFSET);
        return clazzLength;
    }

    @Override
    public int getBodyLength(byte[] header) {
        int bodyLength = bytes2int(header, BODY_LENGTH_OFFSET);
        return bodyLength;
    }

    @Override
    public long getReqId(byte[] header) {
        return bytes2long(header, REQ_ID_LENGTH_OFFSET);
    }


}
