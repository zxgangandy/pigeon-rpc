package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.Pigeon;
import io.andy.pigeon.net.core.config.ClientOption;

import static io.andy.pigeon.net.core.config.ClientOption.CONNECT_TIMEOUT;

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
//        client.oneWayRequest(url, "hello");
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] argv) {
        Pigeon.with(NettyClient.class)
                .serverIp("127.0.0.1")
                .serverPort(8500)
                .config(CONNECT_TIMEOUT, 3000)
                .start()
                .oneWayRequest("hello");


        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
