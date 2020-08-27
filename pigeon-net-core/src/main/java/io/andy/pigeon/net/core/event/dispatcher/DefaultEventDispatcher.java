package io.andy.pigeon.net.core.event.dispatcher;

import io.andy.pigeon.net.core.event.processor.DefaultEventProcessor;
import io.andy.pigeon.net.core.event.processor.EventProcessor;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultEventDispatcher implements EventDispatcher {
    private EventProcessor eventProcessor;

    public DefaultEventDispatcher() {
        this.eventProcessor = new DefaultEventProcessor();
    }

    @Override
    public void dispatch(ChannelHandlerContext ctx) {
        eventProcessor.process(ctx);
    }
}
