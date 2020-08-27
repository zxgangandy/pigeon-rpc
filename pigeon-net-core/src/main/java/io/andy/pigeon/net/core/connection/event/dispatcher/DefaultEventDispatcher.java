package io.andy.pigeon.net.core.connection.event.dispatcher;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;
import io.andy.pigeon.net.core.connection.event.processor.DefaultEventProcessor;
import io.andy.pigeon.net.core.connection.event.processor.EventProcessor;
import io.andy.pigeon.net.core.utils.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultEventDispatcher implements EventDispatcher {
    private EventProcessor eventProcessor;
    private ExecutorService executor;

    public DefaultEventDispatcher() {
        this.eventProcessor = new DefaultEventProcessor();

        executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                new NamedThreadFactory("connection-event-executor", true));
    }

    @Override
    public void dispatch(ConnectionEvent event, Connection connection) {
        executor.execute(()-> eventProcessor.process(event, connection));
    }
}
