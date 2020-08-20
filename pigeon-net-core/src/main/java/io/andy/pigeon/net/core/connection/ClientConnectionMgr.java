package io.andy.pigeon.net.core.connection;

import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.exception.ConnectionException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientConnectionMgr extends AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>  implements ConnectionMgr {
    private static final int MAX_CONNS = 1;
    private ChannelPoolHandler channelPoolHandler;
    private Bootstrap bootstrap;

    private Map<Channel, Connection> connectionMap;

    public ClientConnectionMgr(Bootstrap bootstrap, ChannelPoolHandler channelPoolHandler) {
        this.bootstrap = bootstrap;
        this.channelPoolHandler = channelPoolHandler;

        this.connectionMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Connection connection) {
        connectionMap.put(connection.getChannel(), connection);
    }

    @Override
    public void remove(Connection connection) {
        connectionMap.remove(connection.getChannel());
    }

    @Override
    public Connection get(String key) {
        return null;
    }

    @Override
    public Connection get(Url url) {
        InetSocketAddress connectAddress = new InetSocketAddress(url.getIp(), url.getPort());
        FixedChannelPool pool = get(connectAddress);
        int connectTimeout = 10000;
        Channel ch = null;
        try {
            Long start = System.currentTimeMillis();
            Future<io.netty.channel.Channel> future = pool.acquire();
            boolean ret = future.awaitUninterruptibly(connectTimeout, TimeUnit.MILLISECONDS);
            if (ret && future.isSuccess()) {
                ch = future.getNow();
            } else if (future.cause() != null) {
                String error = "client(url: " + url + ") failed to connect to server or from channelPool "
                        + connectAddress + ", error message is:" + future.cause().getMessage();
                throw new ConnectionException(error, future.cause());
            } else {
                String error = "client(url: " + url + ") failed to connect to server or from channelPool "
                        + connectAddress + " client-side timeout "
                        + connectTimeout + "ms (elapsed: " + (System.currentTimeMillis() - start) + "ms) from client "
                        ;
                throw new ConnectionException(error, future.cause());
            }
        } finally {
            if (ch != null && ch.isActive()) {
                Future<Void> releaseFuture = pool.release(ch);
                releaseFuture.addListener(f -> {
                    if (f.cause() != null) {
                        String error = "client(url: " + url + ") failed to release to channelPool "
                                + connectAddress + ", error message is:" + f.cause().getMessage();
                        log.error(error);
                    }
                });
            }
        }
        return getOrAddConnection(ch, url);
    }

    @Override
    protected FixedChannelPool newPool(InetSocketAddress key) {
        return new FixedChannelPool(bootstrap.remoteAddress(key), channelPoolHandler, MAX_CONNS);
    }

    private Connection getOrAddConnection(Channel ch, Url url) {
        if (ch == null) {
            return null;
        }
        Connection ret = connectionMap.get(ch);
        if (ret == null) {
            Connection connection = new Connection(ch, url);
            if (ch.isActive()) {
                ret = connectionMap.putIfAbsent(ch, connection);
            }
            if (ret == null) {
                ret = connection;
            }
        }
        return ret;
    }
}
