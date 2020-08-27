package io.andy.pigeon.net.core.connection.event.dispatcher;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;

public interface EventDispatcher {
    void dispatch(ConnectionEvent event, Connection connection);
}
