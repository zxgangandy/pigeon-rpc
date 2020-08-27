package io.andy.pigeon.net.core.connection;

import io.andy.pigeon.net.core.base.Url;
import io.netty.channel.Channel;

public interface ConnectionMgr {

    void add(Connection connection);

    void add(Channel channel, Url url);

    void removeIfDisconnected(Channel channel);

    Connection get(Channel channel);

    Connection get(Url url);

}
