package io.andy.pigeon.net.core.connection;

import io.andy.pigeon.net.core.base.Url;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Data
@Slf4j
public class Connection {

    private Channel channel;

    private Url     url;

    public Connection(Channel channel, Url url) {
        this.channel = channel;
        this.url = url;
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
