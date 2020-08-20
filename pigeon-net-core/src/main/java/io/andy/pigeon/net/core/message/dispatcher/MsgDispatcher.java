package io.andy.pigeon.net.core.message.dispatcher;

import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.processor.MsgProcessor;
import io.netty.channel.Channel;

public interface MsgDispatcher {
    void dispatch(Channel ch, Envelope msg, MsgProcessor processor);
}
