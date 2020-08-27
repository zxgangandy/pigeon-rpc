package io.andy.pigeon.net.core.connection;

import io.andy.pigeon.net.core.base.Url;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@Slf4j
public class Connection {

    public static final AttributeKey<Integer> HEARTBEAT_COUNT = AttributeKey.valueOf("heartbeatCount");
    public static final AttributeKey<Connection> CONNECTION   = AttributeKey.valueOf("connection");

    private Lock connectionLock = new ReentrantLock();

    private Channel channel;

    private Url     url;

    public Connection(Channel channel, Url url) {
        this.channel = channel;
        this.url = url;

        this.channel.attr(HEARTBEAT_COUNT).set(0);
        this.channel.attr(CONNECTION).set(this);
    }


    public boolean isWritable() {
        return this.channel.isWritable();
    }


    public InetSocketAddress getLocalAddress() {
        if (channel == null) {
            return null;
        }
        return (InetSocketAddress) this.channel.localAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        if (channel == null) {
            return null;
        }

        return (InetSocketAddress) this.channel.remoteAddress();
    }

    public boolean isActive(){
        return channel.isActive();
    }

    public void close() {
        try {
            if (log.isInfoEnabled()) {
                log.info("Close connection" + channel);
            }
            try {
                channel.close();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }


}
