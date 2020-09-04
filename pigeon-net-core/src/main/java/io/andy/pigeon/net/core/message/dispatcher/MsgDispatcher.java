package io.andy.pigeon.net.core.message.dispatcher;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.message.Envelope;
import io.netty.channel.ChannelHandlerContext;

public interface MsgDispatcher {
    void dispatch(Connection connection, Envelope msg);
}
