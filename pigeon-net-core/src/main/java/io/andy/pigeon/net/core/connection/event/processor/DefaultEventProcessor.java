package io.andy.pigeon.net.core.connection.event.processor;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;
import io.andy.pigeon.net.core.connection.event.listener.EventListenerMgr;
import io.andy.pigeon.net.core.connection.event.listener.IdleEventListener;
import io.andy.pigeon.net.core.utils.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.andy.pigeon.net.core.connection.ConnectionEvent.IDLE;

@Slf4j
public class DefaultEventProcessor implements EventProcessor {

    private EventListenerMgr eventListenerMgr;

    private ExecutorService executor;

    public DefaultEventProcessor() {
        eventListenerMgr = EventListenerMgr.getInstance();
        eventListenerMgr.addEventListener( IDLE, new IdleEventListener());

        executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                new NamedThreadFactory("connection-event-executor", true));
    }

    @Override
    public void process(ConnectionEvent event, Connection connection) {
        executor.execute(()-> {
            eventListenerMgr.onEvent(event, connection);
        });
    }


}
