package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.base.Url;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient extends AbstractClientEndpoint {

    public void oneWay(final Connection conn, final Object request) {
        msgTransmitter.oneWay(conn, request);
    }

    public void oneWay(final Url url, final Object request) {
        msgTransmitter.oneWay(url, request);
    }


}
