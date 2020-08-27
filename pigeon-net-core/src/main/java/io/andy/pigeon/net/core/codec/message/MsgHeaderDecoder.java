package io.andy.pigeon.net.core.codec.message;

public interface MsgHeaderDecoder extends Decoder {

    byte getMagic(byte[] header);

    byte getType(byte[] header);

    byte getCodec(byte[] header);

    int getBodyLength(byte[] header);

    long getReqId(byte[] header);

    short getClazzLength(byte[] header);

}
