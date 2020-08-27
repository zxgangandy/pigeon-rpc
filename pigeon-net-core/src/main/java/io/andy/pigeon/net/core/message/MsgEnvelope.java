package io.andy.pigeon.net.core.message;


import io.andy.pigeon.net.core.utils.ProtoStuffUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;


/***
 * 底层传输消息体封装
 */
@Data
@Accessors(chain = true)
public class MsgEnvelope implements Envelope{

    /**
     * 魔法数 1byte
     */
    private byte magic = (byte) 0xab;

    /**
     * 请求模式 1 byte
     * 0x01 - req - oneWay
     * 0x02 - req - twoWay
     * 0x03 - resp
     * 0x04 - Heartbeat req
     * 0x05 - Heartbeat ack
     */
    private byte type;

    /**
     * 序列化方式 1 byte
     * 0x01 - JSON
     */
    private byte codec;

    /**
     * 请求id， 8位
     */
    private long reqId;

    /** The bodyLength of clazz */
    private short clazzLength = 0;

    /**
     * body长度，4位
     */
    private int bodyLength;

    /** The class of content */
    private byte[] clazz;

    /**
     * 内容体
     */
    private byte[] body;

    @Override
    public void serialize(Object body) {
        this.body = ProtoStuffUtils.serialize(body);
    }

    @Override
    public long getReqId() {
        return reqId;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.of(type);
    }

    @Override
    public String getClazzStr() {
        return new String(clazz, StandardCharsets.UTF_8);
    }

}
