package io.andy.pigeon.net.core.connection.event.listener;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.utils.RemotingUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdleEventListener implements EventListener {

    private Integer maxCount = 5;

    @Override
    public void onEvent(Connection connection) {
        Integer heartbeatTimes = connection.getChannel().attr(Connection.HEARTBEAT_COUNT).get();

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

            connection.sendMsg(heartbeat, new Connection.SendMsgCallback() {
                @Override
                public void onSuccess() {
                    connection.getChannel().attr(Connection.HEARTBEAT_COUNT).set(0);

                    log.debug("Send heartbeat done! Id={}, to remoteAddr={}",
                            heartbeatId, RemotingUtil.parseRemoteAddress(connection.getChannel()));
                }

                @Override
                public void onFailed(Throwable throwable) {
                    Integer times = connection.getChannel().attr(Connection.HEARTBEAT_COUNT).get();
                    connection.getChannel().attr(Connection.HEARTBEAT_COUNT).set(times + 1);

                    log.error("Send heartbeat failed! Id={}, to remoteAddr={}", heartbeatId,
                            RemotingUtil.parseRemoteAddress(connection.getChannel()));
                }
            });
        }
    }
}
