package io.andy.pigeon.net.client;

import io.andy.pigeon.net.core.Pigeon;
import io.andy.pigeon.net.core.client.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static io.andy.pigeon.net.core.config.ClientOption.CONNECT_TIMEOUT;

/**
 * 服务提供者启动类
 *
 */
@SpringBootApplication
public class NettyRpcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcClientApplication.class, args);

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
