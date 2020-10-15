# pigeon-rpc

[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/zxgangandy/pigeon-rpc/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-zxgangandy-orange.svg)](https://github.com/zxgangandy/pigeon-rpc)
[![](https://img.shields.io/badge/version-1.0.2-brightgreen.svg)](https://github.com/zxgangandy/pigeon-rpc)

##简介
pigeon-rpc是一款基于Netty的可以支持百万级别的并发连接的高性能、高度可扩展的的网络通讯框架，主要参考了sofa-bolt的网络通讯模块的设计，
pigeon-rpc可以使用在IM、长连接等领域，也可以在其基础上开发rpc框架。它具有以下的特性：

##项目架构
  - 业务通信层：message->dispatcher->processor->invoker
  - 链接管理层：event->dispatcher->processor->listener

##私有的通讯协议
  - 可定制的编/解码器
  - 支持多种序列化机制
##客户端/服务端连接管理
  - 连接的心跳和空闲检测
  - 客户端连接池
  - 自动断连和重连
##丰富的通信模型
  - oneway
  - twoway（sync、async）
##easy to use
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
