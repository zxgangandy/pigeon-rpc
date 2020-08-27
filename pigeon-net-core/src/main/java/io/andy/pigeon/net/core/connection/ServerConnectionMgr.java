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
    public void add(Channel channel, Url url) {
        if (null == channel || url == null) {
            return;
        }

        connectionMap.putIfAbsent(channel, new Connection(channel, url));
    }

    @Override
    public void removeIfDisconnected(Channel channel) {
        if (channel != null && !channel.isActive()) {
            connectionMap.remove(channel);
        }
    }

    @Override
    public Connection get(Channel channel) {
        return connectionMap.get(channel);
    }

    @Override
    public Connection get(Url url) {
        return null;
    }


}
