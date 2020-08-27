package io.andy.pigeon.net.core.codec;

import io.andy.pigeon.net.core.codec.message.MsgCodecFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NettyDecoder extends ByteToMessageDecoder {
    private MsgCodecFactory codecFactory;

    public NettyDecoder(MsgCodecFactory codecFactory) {
        this.codecFactory = codecFactory;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        codecFactory.getDecoder().decode(in, out);
    }
}
