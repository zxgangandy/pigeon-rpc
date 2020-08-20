
package io.andy.pigeon.net.core.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ThreadFactory;

/**
 * Utils for netty EventLoop
 *
 */
public class NettyEventLoopUtil {

    /** check whether epoll enabled, and it would not be changed during runtime. */
    private static boolean epollEnabled =  Epoll.isAvailable();

    /**
     * Create the right event loop according to current platform and system property, fallback to NIO when epoll not enabled.
     *
     * @param nThreads
     * @param threadFactory
     * @return an EventLoopGroup suitable for the current platform
     */
    public static EventLoopGroup newEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
        return epollEnabled ? new EpollEventLoopGroup(nThreads, threadFactory)
            : new NioEventLoopGroup(nThreads, threadFactory);
    }

    /**
     * @return a SocketChannel class suitable for the given EventLoopGroup implementation
     */
    public static Class<? extends SocketChannel> getClientSocketChannelClass() {
        return epollEnabled ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    /**
     * @return a ServerSocketChannel class suitable for the given EventLoopGroup implementation
     */
    public static Class<? extends ServerSocketChannel> getServerSocketChannelClass() {
        return epollEnabled ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    /**
     * Use {@link EpollMode#LEVEL_TRIGGERED} for server bootstrap if level trigger enabled by system properties,
     *   otherwise use {@link EpollMode#EDGE_TRIGGERED}.
     * @param serverBootstrap server bootstrap
     */
    public static void enableTriggeredMode(ServerBootstrap serverBootstrap) {
        if (epollEnabled) {
            serverBootstrap
                    .childOption(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED);
        }
    }
}
