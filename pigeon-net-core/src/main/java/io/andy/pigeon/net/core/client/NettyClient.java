package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.config.Option;
import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.Url;
import io.andy.pigeon.net.core.message.RespMsg;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient extends AbstractClientEndpoint {

    @Override
    public NettyClient start() {
        super.start();

        return this;
    }

    public NettyClient serverIp(String serverIp) {
        this.serverIp = serverIp;
        return this;
    }

    public NettyClient serverPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    @Override
    public <T> NettyClient config(Option<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        this.options.option(option, value);
        return this;
    }

    public void oneWayRequest(final Connection conn, final Object request) {
        sendOneWayMsg(conn, request);
    }

    public void oneWayRequest(final Object request) {
        checkStarted();
        Url url = new Url(serverIp, serverPort);
        sendOneWayMsg(url, request);
    }

    public RespMsg syncRequest(final Url url, final Object request)  {
        checkStarted();
        try {
            return sendSyncMsg(url, request);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RespMsg asyncRequest(final Url url, final Object request)  {
        checkStarted();
        try {
            return sendSyncMsg(url, request);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
