package io.andy.pigeon.net.core.message.transmitter;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionMgr;
import io.andy.pigeon.net.core.base.Url;

public class ClientMsgTransmitter extends BaseMsgTransmitter {

    public ClientMsgTransmitter(ConnectionMgr connectionMgr) {
        super(connectionMgr);
    }

    public void oneWay(final Url url, final Object request) {
        Connection conn = connectionMgr.get(url);
        oneWay(conn, request);
    }



}
