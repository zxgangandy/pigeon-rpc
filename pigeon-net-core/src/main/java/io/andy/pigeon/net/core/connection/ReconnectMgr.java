package io.andy.pigeon.net.core.connection;

import io.andy.pigeon.net.core.Url;

public interface ReconnectMgr {
    void reconnect(Url url);
}
