package io.andy.pigeon.net.core.handler;

import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.connection.ClientConnectionMgr;
import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;
import io.andy.pigeon.net.core.connection.ConnectionMgr;
import io.andy.pigeon.net.core.connection.event.dispatcher.DefaultEventDispatcher;
import io.andy.pigeon.net.core.connection.event.dispatcher.EventDispatcher;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.dispatcher.DefaultMsgDispatcher;
import io.andy.pigeon.net.core.message.dispatcher.MsgDispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

import static io.andy.pigeon.net.core.connection.ConnectionEvent.IDLE;

@ChannelHandler.Sharable
@Slf4j
public class NettyMessageHandler extends SimpleChannelInboundHandler<Envelope> {

    private boolean       serverSide;
    private ConnectionMgr connectionMgr;
    private MsgDispatcher msgDispatcher;
    private EventDispatcher eventDispatcher;

    public NettyMessageHandler(boolean serverSide, ConnectionMgr connectionMgr) {
        this.serverSide = serverSide;
        this.connectionMgr = connectionMgr;
        this.msgDispatcher = new DefaultMsgDispatcher(serverSide);
        this.eventDispatcher = new DefaultEventDispatcher();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Envelope envelope) throws Exception {
        log.info("envelope=" + envelope);
        msgDispatcher.dispatch(ctx, envelope);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            log.error("Channel exception ,peer={}, cause={} ",ctx.channel().remoteAddress(), cause);
        }finally {
            connectionMgr.removeIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive=");

        if (serverSide) {
            InetSocketAddress peerAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            connectionMgr.add(ctx.channel(), new Url(peerAddress.getHostName(), peerAddress.getPort()));
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive=");
        connectionMgr.removeIfDisconnected(ctx.channel());
        userEventTriggered(ctx, ConnectionEvent.CLOSE);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            processIdleEvent(ctx, evt);
        }  else if (evt instanceof ConnectionEvent) {
            processConnectionEvent(ctx, evt);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * Only client dispatch the idle event, the server side may close the connection in some cases.
     * But here we do no thing, except logging.
     */
    private void processIdleEvent(ChannelHandlerContext ctx, Object evt) {
        if (serverSide) {
            log.info("The server side receive idle evt={}", evt);
        } else {
            log.info("The client side receive idle evt={}", evt);
            Connection connection = ctx.channel().attr(Connection.CONNECTION).get();
            if (connection == null) {
                log.error("[BUG]connection is null when handle user IdleStateEvent event!");
                return;
            }

            eventDispatcher.dispatch(IDLE, connection);
        }
    }

    private void processConnectionEvent(ChannelHandlerContext ctx, Object evt) {
        if (!serverSide) {
            ClientConnectionMgr clientConnectionMgr = (ClientConnectionMgr) connectionMgr;
            ConnectionEvent eventType = (ConnectionEvent) evt;
            Connection connection = ctx.channel().attr(Connection.CONNECTION).get();

            if (connection == null) {
                log.error("[BUG]connection is null when handle connection event!");
                return;
            }

            switch (eventType) {
                case CONNECT:
                    eventDispatcher.dispatch(eventType, connection);
                    break;
                case CONNECT_FAILED:
                case CLOSE:
                    clientConnectionMgr.reconnect(connection.getUrl());
                    break;
                default:
                    log.error("[BUG]unknown event: {}", eventType.name());
                    break;
            }
        }
    }

}
