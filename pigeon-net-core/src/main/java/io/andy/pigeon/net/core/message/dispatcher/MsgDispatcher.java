package io.andy.pigeon.net.core.message.dispatcher;

import io.andy.pigeon.net.core.message.Envelope;
import io.netty.channel.ChannelHandlerContext;

public interface MsgDispatcher {
    void dispatch(ChannelHandlerContext ctx, Envelope msg);
}
