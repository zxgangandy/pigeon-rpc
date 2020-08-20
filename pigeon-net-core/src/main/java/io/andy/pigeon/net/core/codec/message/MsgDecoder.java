package io.andy.pigeon.net.core.codec.message;

import io.netty.buffer.ByteBuf;

import java.util.List;

public interface MsgDecoder extends Decoder{
    /**
     * 解码
     */
    void decode(ByteBuf in, List<Object> out);
}
