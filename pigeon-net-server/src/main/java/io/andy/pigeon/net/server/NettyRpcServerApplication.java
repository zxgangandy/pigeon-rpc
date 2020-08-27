package io.andy.pigeon.net.server;

import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.client.NettyClient;
import io.andy.pigeon.net.core.config.ClientOption;
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

        NettyServer server = new NettyServer();
        server.start();
    }

}
