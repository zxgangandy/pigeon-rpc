package io.andy.pigeon.net.core.message.transmitter;

import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.connection.ConnectionMgr;

public class ServerMsgTransmitter extends BaseMsgTransmitter {
    public ServerMsgTransmitter(ConnectionMgr connectionMgr) {
        super(connectionMgr);
    }

    @Override
    public void oneWay(Url url, Object request) {

    }
}
