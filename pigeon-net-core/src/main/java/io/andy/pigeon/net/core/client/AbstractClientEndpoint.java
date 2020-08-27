package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.base.AbstractRemotingEndpoint;
import io.andy.pigeon.net.core.codec.message.DefaultMsgCodecFactory;
import io.andy.pigeon.net.core.codec.message.MsgCodecFactory;
import io.andy.pigeon.net.core.codec.NettyDecoder;
import io.andy.pigeon.net.core.codec.NettyEncoder;
import io.andy.pigeon.net.core.connection.ClientConnectionMgr;
import io.andy.pigeon.net.core.connection.ConnectionMgr;
import io.andy.pigeon.net.core.constant.Constants;
import io.andy.pigeon.net.core.handler.NettyMessageHandler;
import io.andy.pigeon.net.core.message.emitter.BaseMsgEmitter;
import io.andy.pigeon.net.core.message.emitter.ClientMsgEmitter;
import io.andy.pigeon.net.core.utils.NamedThreadFactory;
import io.andy.pigeon.net.core.utils.NettyEventLoopUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractClientEndpoint extends AbstractRemotingEndpoint {

    private static final EventLoopGroup workerGroup = NettyEventLoopUtil.newEventLoopGroup(
            Runtime.getRuntime().availableProcessors() + 1,
            new NamedThreadFactory("netty-client-worker", true));

    private MsgCodecFactory codecFactory;
    protected BaseMsgEmitter msgEmitter;
    private ConnectionMgr connectionMgr;

    private Bootstrap bootstrap;

    @Override
    public void start() {
        initialize();

        this.codecFactory = new DefaultMsgCodecFactory();
        this.connectionMgr = new ClientConnectionMgr(bootstrap, getChannelPoolHandler());
        this.msgEmitter = new ClientMsgEmitter(connectionMgr);
    }

    @Override
    public void stop() {

    }

    private void initialize() {
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    private ChannelPoolHandler getChannelPoolHandler() {
        return new ChannelPoolHandler() {
            @Override
            public void channelReleased(Channel channel) throws Exception {
                log.debug("client channel pool release id {}",channel.id());
            }

            @Override
            public void channelAcquired(Channel channel) throws Exception {
                log.debug("client channel pool acquired id {}",channel.id());
            }

            @Override
            public void channelCreated(Channel channel) throws Exception {
                log.debug("client channel pool created id {}",channel.id());

                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast("d", new NettyDecoder(codecFactory));
                pipeline.addLast("e", new NettyEncoder(codecFactory));

                pipeline.addLast("i", new IdleStateHandler(
                        Constants.CLIENT_READER_IDLE_TIME_SECONDS,
                        Constants.CLIENT_WRITER_IDLE_TIME_SECONDS,
                        Constants.CLIENT_ALL_IDLE_TIME_SECONDS,
                        TimeUnit.SECONDS)
                );

                pipeline.addLast("m", new NettyMessageHandler(false, connectionMgr));
            }
        };
    }
}
