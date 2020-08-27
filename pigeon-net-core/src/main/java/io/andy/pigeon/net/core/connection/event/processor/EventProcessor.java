package io.andy.pigeon.net.core.connection.event.processor;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;

public interface EventProcessor {

    void process(ConnectionEvent event, Connection connection);

}
