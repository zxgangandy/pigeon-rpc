package io.andy.pigeon.net.core.event.dispatcher;

import io.netty.channel.ChannelHandlerContext;

public interface EventDispatcher {
    void dispatch(ChannelHandlerContext ctx);
}
