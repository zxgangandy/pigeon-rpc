package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.config.ClientOption;
import io.andy.pigeon.net.core.server.NettyServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//@RunWith(JUnit4.class)
public class NettyClientTest {
//    NettyClient client;
//    NettyServer server;
//
//    @Before
//    public void setUp() {
////        server = new NettyServer();
////        server.start();
//
//        client = new NettyClient();
//        client.option(ClientOption.CONNECT_TIMEOUT, 3000);
//        client.start();
//    }
//
//    @Test
//    public void test_oneway() {
//        Url url = Url.builder().ip("127.0.0.1")
//                .port(8500)
//                .build();
//
//        client.oneWay(url, "hello");
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] argv) {
        NettyClient client = new NettyClient();
        client.option(ClientOption.CONNECT_TIMEOUT, 3000);
        client.start();

        Url url = Url.builder().ip("127.0.0.1")
                .port(8500)
                .build();

        for (int i=0; i< 1; i++) {
            client.oneWay(url, "hello" + i);
        }

        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
