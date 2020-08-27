package io.andy.pigeon.net.core.base;

import io.andy.pigeon.net.core.config.Configurable;
import io.andy.pigeon.net.core.config.Option;
import io.andy.pigeon.net.core.config.Options;
import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionMgr;
import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRemotingEndpoint implements RemotingEndpoint, Configurable {

    private final Options options = new Options();
    protected ConnectionMgr connectionMgr;

    @Override
    public <T> Configurable option(Option<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        this.options.option(option, value);
        return this;
    }

    @Override
    public <T> T option(Option<T> option) {
        return options.option(option);
    }


    public void sendOneWayMsg(final Connection conn, final Object request) {
        Envelope envelope = DefaultMsgFactory.getInstance().createOneWay(request);
        try {
            conn.getChannel().writeAndFlush(envelope).addListener((ChannelFuture f) -> {
                if (!f.isSuccess()) {
                    log.error("One way send failed. The address is {}",
                            RemotingUtil.parseRemoteAddress(conn.getChannel()), f.cause());
                } else {
                    log.info("One way send success!!");
                }
            });
        } catch (Exception e) {
            if (null == conn) {
                log.error("Conn is null");
            } else {
                log.error("Exception caught when sending invocation. The address is {}",
                        RemotingUtil.parseRemoteAddress(conn.getChannel()), e);
            }
        }
    }


    public void sendOneWayMsg(final Url url, final Object request) {
        Connection conn = connectionMgr.get(url);
        sendOneWayMsg(conn, request);
    }

}
