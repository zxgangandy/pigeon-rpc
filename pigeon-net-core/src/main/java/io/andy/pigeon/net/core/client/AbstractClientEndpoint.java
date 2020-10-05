package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.AbstractRemotingEndpoint;
import io.andy.pigeon.net.core.Url;
import io.andy.pigeon.net.core.codec.message.DefaultMsgCodecFactory;
import io.andy.pigeon.net.core.codec.message.MsgCodecFactory;
import io.andy.pigeon.net.core.codec.NettyDecoder;
import io.andy.pigeon.net.core.codec.NettyEncoder;
import io.andy.pigeon.net.core.connection.ClientConnectionMgr;
import io.andy.pigeon.net.core.constant.Constants;
import io.andy.pigeon.net.core.exception.StartException;
import io.andy.pigeon.net.core.handler.NettyMessageHandler;
import io.andy.pigeon.net.core.utils.NamedThreadFactory;
import io.andy.pigeon.net.core.utils.NettyEventLoopUtil;
import io.andy.pigeon.net.core.utils.StringUtils;
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

    protected String serverIp;

    protected int serverPort;

    private MsgCodecFactory codecFactory;

    private Bootstrap bootstrap;

    protected Url serverUrl;

    @Override
    public AbstractClientEndpoint start() {
        super.start();

        initialize();

        return this;
    }

    @Override
    public void stop() {
        super.stop();
    }

    private void initialize() {
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        this.codecFactory = new DefaultMsgCodecFactory();
        this.connectionMgr = new ClientConnectionMgr(bootstrap, getChannelPoolHandler());
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

    protected void checkStarted() {
        if (!started()) {
            throw new StartException(String.format("Client has not been started yet!"));
        }
    }

    protected void checkServerUrl() {
        if (StringUtils.isNotEmpty(this.serverIp) && this.serverPort > 0) {
            serverUrl = new Url(serverIp, serverPort);
        } else {
            throw new IllegalArgumentException("Server ip is empty or server port is invalid");
        }
    }

}
