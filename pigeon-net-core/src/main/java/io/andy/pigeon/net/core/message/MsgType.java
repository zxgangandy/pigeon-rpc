package io.andy.pigeon.net.core.message;

/**
 * 传输类型
 * @author andy
 */
public enum MsgType {

    /**
     * req - oneWay
     */
    REQ_ONE_WAY((byte)0x01),

    /**
     * req - twoWay
     */
    REQ_TWO_WAY((byte)0x02),

    /**
     * resp
     */
    MODE_RESP((byte)0x03),

    /**
     * heartbeat
     */
    MODE_HEARTBEAT((byte)0x04),
    ;

    private byte type;

    MsgType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

}
