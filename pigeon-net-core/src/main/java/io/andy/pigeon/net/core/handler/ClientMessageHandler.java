package io.andy.pigeon.net.core.handler;

import io.andy.pigeon.net.core.message.Envelope;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@ChannelHandler.Sharable
@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<Envelope> {

    public ClientMessageHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Envelope envelope) throws Exception {

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("error acquire channel " + ctx.channel().remoteAddress(), cause);
    }
}
