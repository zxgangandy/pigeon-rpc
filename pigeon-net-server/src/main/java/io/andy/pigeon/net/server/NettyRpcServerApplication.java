package io.andy.pigeon.net.server;

import io.andy.pigeon.net.core.Pigeon;
import io.andy.pigeon.net.core.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务提供者启动类
 *
 */
@SpringBootApplication
public class NettyRpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcServerApplication.class, args);

        Pigeon.with(NettyServer.class)
                .start();
    }

}
