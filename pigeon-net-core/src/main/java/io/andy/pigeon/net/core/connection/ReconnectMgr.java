package io.andy.pigeon.net.core.connection;

import io.andy.pigeon.net.core.base.Url;

public interface ReconnectMgr {
    void reconnect(Url url);
}
