package io.andy.pigeon.net.core.message.processor;

import io.andy.pigeon.net.core.message.Envelope;
import io.netty.channel.Channel;

public interface MsgProcessor {

    Envelope process(Envelope req, Channel ioChannel);

}
