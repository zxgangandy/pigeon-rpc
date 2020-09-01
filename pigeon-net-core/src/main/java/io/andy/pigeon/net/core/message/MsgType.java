package io.andy.pigeon.net.core.message;

/**
 * 传输类型
 * @author andy
 */
public enum MsgType {

    /**
     * req - oneWayRequest
     */
    REQ_ONE_WAY((byte)0x01),

    /**
     * req - twoWay
     */
    REQ_TWO_WAY((byte)0x02),

    /**
     * ack - twoWay
     */
    ACK_TWO_WAY((byte)0x03),

    /**
     * heartbeat req
     */
    REQ_HEARTBEAT((byte)0x04),

    /**
     * heartbeat ack
     */
    ACK_HEARTBEAT((byte)0x05),
    ;

    private byte type;

    MsgType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public static MsgType of(byte type) {
        for (MsgType msgType : values()) {
            if (msgType.getType() == type) {
                return msgType;
            }
        }

        return null;
    }
}
