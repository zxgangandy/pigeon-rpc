package io.andy.pigeon.net.core.connection.event.listener;

import io.andy.pigeon.net.core.connection.Connection;

public interface EventListener {

    void onEvent(Connection connection);
}
