package io.andy.pigeon.net.core.connection.event.processor;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;
import io.andy.pigeon.net.core.connection.event.listener.EventListenerMgr;
import io.andy.pigeon.net.core.connection.event.listener.IdleEventListener;
import lombok.extern.slf4j.Slf4j;

import static io.andy.pigeon.net.core.connection.ConnectionEvent.IDLE;

@Slf4j
public class DefaultEventProcessor implements EventProcessor {

    private EventListenerMgr eventListenerMgr;

    public DefaultEventProcessor() {
        eventListenerMgr = EventListenerMgr.getInstance();
        eventListenerMgr.addEventListener( IDLE, new IdleEventListener());
    }

    @Override
    public void process(ConnectionEvent event, Connection connection) {
        log.info("In event processor, the event={}", event);
        eventListenerMgr.onEvent(event, connection);
    }
}
