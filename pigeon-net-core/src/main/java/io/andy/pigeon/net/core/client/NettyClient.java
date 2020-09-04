package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.config.Option;
import io.andy.pigeon.net.core.message.RespMsg;
import io.andy.pigeon.net.core.message.invoker.DefaultInvokerMgr;
import io.andy.pigeon.net.core.message.invoker.InvokeFuture;
import io.andy.pigeon.net.core.message.invoker.Invoker;
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

        checkServerUrl();
        return this;
    }

    public NettyClient serverPort(int serverPort) {
        this.serverPort = serverPort;

        checkServerUrl();
        return this;
    }

    @Override
    public <T> NettyClient config(Option<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        this.options.option(option, value);
        return this;
    }

    public NettyClient invoker(Invoker invoker) {
        DefaultInvokerMgr.getInstance().setInvoker(invoker);
        return this;
    }

    public void oneWayRequest(final Object request) {
        checkStarted();
        sendOneWayMsg(serverUrl, request);
    }

    public RespMsg twoWayRequest(final Object request)  {
        checkStarted();
        try {
            return sendSyncMsg(serverUrl, request);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InvokeFuture twoWayRequest(final Object request, final int timeoutMillis)  {
        checkStarted();
        try {
            return sendAsyncMsg(serverUrl, request, timeoutMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
