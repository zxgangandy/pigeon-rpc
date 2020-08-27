package io.andy.pigeon.net.core.server;

import io.andy.pigeon.net.core.AbstractRemotingEndpoint;
import io.andy.pigeon.net.core.codec.message.DefaultMsgCodecFactory;
import io.andy.pigeon.net.core.codec.message.MsgCodecFactory;
import io.andy.pigeon.net.core.codec.NettyDecoder;
import io.andy.pigeon.net.core.codec.NettyEncoder;
import io.andy.pigeon.net.core.config.*;
import io.andy.pigeon.net.core.connection.ServerConnectionMgr;
import io.andy.pigeon.net.core.constant.Constants;
import io.andy.pigeon.net.core.handler.NettyMessageHandler;
import io.andy.pigeon.net.core.utils.NamedThreadFactory;
import io.andy.pigeon.net.core.utils.NettyEventLoopUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractServerEndpoint extends AbstractRemotingEndpoint {
    /** message codec factory */
    private MsgCodecFactory             codecFactory;

    /** server bootstrap */
    private ServerBootstrap             bootstrap;

    /** channelFuture */
    private ChannelFuture               channelFuture;

    /** boss event loop group, boss group should not be daemon, need shutdown manually*/
    private final EventLoopGroup        bossGroup   = NettyEventLoopUtil.newEventLoopGroup(
            1,
            new NamedThreadFactory("netty-server-boss", false));
    /** worker event loop group. Reuse I/O worker threads between rpc servers. */
    private static final EventLoopGroup workerGroup =  NettyEventLoopUtil.newEventLoopGroup(
            Runtime.getRuntime().availableProcessors() * 2,
            new NamedThreadFactory("netty-server-worker", true));

    @Override
    public void start()  {
        this.codecFactory = new DefaultMsgCodecFactory();
        this.connectionMgr = new ServerConnectionMgr();

        startup();

        log.info("Netty server started!!");
    }

    @Override
    public void stop() {
        if (null != this.channelFuture) {
            this.channelFuture.channel().close();
        }

        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    private void startup() {
        initialize();

        try {
            bind();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端各种初始化工作
     */
    private void initialize() {
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast("d", new NettyDecoder(codecFactory));
                        pipeline.addLast("e", new NettyEncoder(codecFactory));

                        pipeline.addLast("i", new IdleStateHandler(
                                Constants.SERVER_READER_IDLE_TIME_SECONDS,
                                Constants.SERVER_WRITER_IDLE_TIME_SECONDS,
                                Constants.SERVER_ALL_IDLE_TIME_SECONDS,
                                TimeUnit.SECONDS)
                        );

                        pipeline.addLast("m", new NettyMessageHandler(true, connectionMgr));
                    }

                });

    }

    /**
     * 服务端绑定端口
     */
    private void bind() throws InterruptedException {
        final int port = getBindPort();
        channelFuture = bootstrap.bind(port).sync();
        try {
            if (channelFuture.isSuccess()) {
                log.info("Server started on port {}", port);
            } else {
                log.warn("Failed starting server on port {}", port);
                throw new IllegalStateException("Failed starting server on port: " + port);
            }
        } catch (Throwable e) {
            throw new IllegalStateException("ERROR: Failed to start server!!", e);
        }

        Channel channel = channelFuture.channel();
        channel.closeFuture().sync();
    }


    private int getBindPort() {
        return option(ServerOption.PORT);
    }
}
