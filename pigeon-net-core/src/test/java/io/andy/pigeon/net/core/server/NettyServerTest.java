package io.andy.pigeon.net.core.server;

import io.andy.pigeon.net.core.Pigeon;
import io.andy.pigeon.net.core.client.DefaultInvoker;

public class NettyServerTest {

    public static void main(String[] argv) {
//        NettyServer server = new NettyServer();
//        server.start();

        Pigeon.with(NettyServer.class)
                .invoker(new DefaultInvoker())
                .start();
    }
}
