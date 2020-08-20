package io.andy.pigeon.net.core.codec.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface MsgEncoder<T> {
    /**
     * 编码方法
     * @param msg 待编码的对象
     * @return 变化后转换成的字节数组
     */
    void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception;
}
