package io.andy.pigeon.net.client;

import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.client.NettyClient;
import io.andy.pigeon.net.core.config.ClientOption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务提供者启动类
 *
 */
@SpringBootApplication
public class NettyRpcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcClientApplication.class, args);

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
