package io.andy.pigeon.net.core.message;

/***
 *  消息打包接口
 */
public interface Envelope {

    void serialize(Object body);

    long getReqId();

}
