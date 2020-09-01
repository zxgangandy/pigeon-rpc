package io.andy.pigeon.net.core.server;

import io.andy.pigeon.net.core.Pigeon;

public class NettyServerTest {

    public static void main(String[] argv) {
//        NettyServer server = new NettyServer();
//        server.start();

        Pigeon.with(NettyServer.class)
                .start();
    }
}
