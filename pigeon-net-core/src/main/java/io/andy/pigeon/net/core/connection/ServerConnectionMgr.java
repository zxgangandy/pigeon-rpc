package io.andy.pigeon.net.core.connection;


import io.andy.pigeon.net.core.base.Url;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnectionMgr implements ConnectionMgr {

    private Map<Channel, Connection> connectionMap;

    public ServerConnectionMgr() {
        this.connectionMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Connection connection) {
        if (null == connection) {
            return;
        }

        connectionMap.putIfAbsent(connection.getChannel(), connection);
    }

    @Override
    public void remove(Connection connection) {
        if (null == connection) {
            return;
        }

        connectionMap.remove(connection.getChannel());
    }

    @Override
    public Connection get(String key) {
        return connectionMap.get(key);
    }

    @Override
    public Connection get(Url url) {
        return null;
    }


}
