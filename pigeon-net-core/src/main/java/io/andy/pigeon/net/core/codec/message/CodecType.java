package io.andy.pigeon.net.core.codec.message;


import lombok.Getter;

/**
 * 序列化类型
 * @author andy
 */

@Getter
public enum CodecType {
    JSON((byte)0x01, "json", "JSON序列化"),
    KRYO((byte)0x02, "kryo", "KRYO序列化"),
    PROTO_STUFF((byte)0x03, "protoStuff", "ProtoStuff序列化");

    private byte type;

    private String name;

    private String desc;

    CodecType(byte type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

}
