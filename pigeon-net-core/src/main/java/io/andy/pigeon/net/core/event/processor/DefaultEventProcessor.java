package io.andy.pigeon.net.core.event.processor;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultEventProcessor implements EventProcessor {
    private Integer maxCount = 5;

    @Override
    public void process(ChannelHandlerContext ctx) {
        Integer heartbeatTimes = ctx.channel().attr(Connection.HEARTBEAT_COUNT).get();
        Connection connection = ctx.channel().attr(Connection.CONNECTION).get();

        if (heartbeatTimes >= maxCount) {
            try {
                if (connection.isActive()) {
                    connection.close();
                }

                log.error("Heartbeat failed for {} times, close the connection from client side: {} ",
                        heartbeatTimes, RemotingUtil.parseRemoteAddress(connection.getChannel()));
            } catch (Exception e) {
                log.warn("Exception caught when closing connection in SharableHandler.", e);
            }
        } else {
            Envelope heartbeat = DefaultMsgFactory.getInstance().createHeartbeatReq();
            long heartbeatId = heartbeat.getReqId();

            ctx.writeAndFlush(heartbeat).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        ctx.channel().attr(Connection.HEARTBEAT_COUNT).set(0);

                        log.debug("Send heartbeat done! Id={}, to remoteAddr={}",
                                heartbeatId, RemotingUtil.parseRemoteAddress(connection.getChannel()));
                    } else {
                        Integer times = ctx.channel().attr(Connection.HEARTBEAT_COUNT).get();
                        ctx.channel().attr(Connection.HEARTBEAT_COUNT).set(times + 1);

                        log.error("Send heartbeat failed! Id={}, to remoteAddr={}", heartbeatId,
                                RemotingUtil.parseRemoteAddress(connection.getChannel()));
                    }
                }
            });
        }
    }


}
