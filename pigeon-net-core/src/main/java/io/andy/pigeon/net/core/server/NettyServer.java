package io.andy.pigeon.net.core.server;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.Url;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyServer extends AbstractServerEndpoint {

    public void oneWay(final Connection conn, final Object request) {
        sendOneWayMsg(conn, request);
    }

    public void oneWay(final Url url, final Object request) {
        sendOneWayMsg(url, request);
    }

}
