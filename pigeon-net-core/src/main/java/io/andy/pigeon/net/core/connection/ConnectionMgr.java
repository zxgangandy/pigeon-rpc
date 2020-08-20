package io.andy.pigeon.net.core.connection;

import io.andy.pigeon.net.core.base.Url;

public interface ConnectionMgr {

    void add(Connection connection);


    void remove(Connection connection);

    Connection get(String key);

    Connection get(Url url);
}
