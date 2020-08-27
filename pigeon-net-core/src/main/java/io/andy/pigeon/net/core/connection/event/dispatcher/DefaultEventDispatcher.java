package io.andy.pigeon.net.core.connection.event.dispatcher;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;
import io.andy.pigeon.net.core.connection.event.processor.DefaultEventProcessor;
import io.andy.pigeon.net.core.connection.event.processor.EventProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultEventDispatcher implements EventDispatcher {
    private EventProcessor eventProcessor;

    public DefaultEventDispatcher() {
        this.eventProcessor = new DefaultEventProcessor();
    }

    @Override
    public void dispatch(ConnectionEvent event, Connection connection) {
        eventProcessor.process(event, connection);
    }
}
