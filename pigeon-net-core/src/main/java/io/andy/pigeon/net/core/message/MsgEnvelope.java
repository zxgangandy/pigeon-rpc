package io.andy.pigeon.net.core.message;


import io.andy.pigeon.net.core.utils.ProtoStuffUtils;
import lombok.Data;
import lombok.experimental.Accessors;


/***
 * 底层传输消息体封装
 */
@Data
@Accessors(chain = true)
public class MsgEnvelope implements Envelope{

    /**
     * 魔法数 1位
     */
    private byte magic = (byte) 0xab;

    /**
     * 请求模式 1位
     * 0x01 - req - oneWay
     * 0x02 - req - twoWay
     * 0x03 - resp
     * 0x04 - Heartbeat
     */
    private byte type;

    /**
     * 序列化方式 1位
     * 0x01 - JSON
     */
    private byte codec;

    /**
     * 请求id， 8位
     */
    private long reqId;

    /**
     * body长度，4位
     */
    private int length;

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


}
