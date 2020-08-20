package io.andy.pigeon.net.core.codec;

import io.andy.pigeon.net.core.codec.message.MsgCodecFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class NettyEncoder extends MessageToByteEncoder<Envelope> {
    private MsgCodecFactory codec;

    public NettyEncoder(MsgCodecFactory codec) {
        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Envelope msg, ByteBuf out) throws Exception {
        codec.getEncoder().encode(ctx, msg, out);
    }
}
