# pigeon-rpc
pigeon-rpc

pigeon-rpc是一款基于Netty的可以支持百万级别的并发连接的高性能、高度可扩展的的网络通讯框架，主要参考了sofa-bolt的网络通讯模块的设计，
pigeon-rpc可以使用在IM、长连接等领域，也可以在其基础上开发rpc框架。它具有以下的特性：

- 私有的通讯协议
  - 可定制的编/解码器
  - 支持多种序列化机制
- 客户端/服务端连接管理
  - 连接的心跳和空闲检测
  - 客户端连接池
  - 自动断连和重连
- 丰富的通信模型
  - oneway
  - twoway（sync、async）
- easy to use
  - 客户端例子：
  
  ``` java
  Pigeon.with(NettyClient.class)
                    .serverIp("127.0.0.1")
                    .serverPort(8500)
                    .config(CONNECT_TIMEOUT, 3000)
                    .start()
                    .oneWayRequest("hello");
  
  ```
  - 服务端例子
  
    ``` java
    Pigeon.with(NettyServer.class)
                    .start();
    
    ```
