package io.andy.pigeon.net.core.codec.message;

public interface Decoder {
    int MAGIC_VALUE_SIZE = 1;
    int TYPE_VALUE_SIZE = 1;
    int CODEC_VALUE_SIZE = 1;
    int REQ_ID_VALUE_SIZE = 8;
    int CLAZZ_LENGTH_SIZE = 2;
    int BODY_LENGTH_SIZE = 4;


    byte MAGIC = (byte) 0xab;


    default int getMsgHeaderSize() {
        return MAGIC_VALUE_SIZE
                + TYPE_VALUE_SIZE
                + CODEC_VALUE_SIZE
                + REQ_ID_VALUE_SIZE
                + CLAZZ_LENGTH_SIZE
                + BODY_LENGTH_SIZE;
    }
}
