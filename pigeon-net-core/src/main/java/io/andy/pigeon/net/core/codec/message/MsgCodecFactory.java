package io.andy.pigeon.net.core.codec.message;

/**
 * @Author: andy
 * @Date: 2020/08/11 22:05
 * @Description: 序列化，包含编码器和解码器
 */
public interface MsgCodecFactory {
    /**
     * 获取编码器
     *
     * @return MsgEncoder
     */
    MsgEncoder getEncoder();

    /**
     * 获取解码器
     *
     * @return MsgDecoder
     */
    MsgDecoder getDecoder();
}
