package io.andy.pigeon.net.core.message.processor;

import io.andy.pigeon.net.core.message.Envelope;
import io.netty.channel.Channel;

public class ClientMsgProcessor implements MsgProcessor {
    @Override
    public Envelope process(Envelope req, Channel ioChannel) {
        return null;
    }
}
