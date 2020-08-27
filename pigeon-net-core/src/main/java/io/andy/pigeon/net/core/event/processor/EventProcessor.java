package io.andy.pigeon.net.core.event.processor;

import io.netty.channel.ChannelHandlerContext;

public interface EventProcessor {

    void process(final ChannelHandlerContext ctx);

}
