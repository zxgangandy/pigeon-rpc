package io.andy.pigeon.net.core.server;

import io.andy.pigeon.net.core.config.Option;
import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.Url;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyServer extends AbstractServerEndpoint {

    @Override
    public <T> NettyServer config(Option<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        this.options.option(option, value);
        return this;
    }

    public void oneWay(final Connection conn, final Object request) {
        checkStarted();
        sendOneWayMsg(conn, request);
    }

    public void oneWay(final Url url, final Object request) {
        checkStarted();
        sendOneWayMsg(url, request);
    }

}
