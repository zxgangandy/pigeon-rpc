package io.andy.pigeon.net.core.message.emitter;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionMgr;
import io.andy.pigeon.net.core.base.Url;

public class ClientMsgEmitter extends BaseMsgEmitter {

    public ClientMsgEmitter(ConnectionMgr connectionMgr) {
        super(connectionMgr);
    }

    public void oneWay(final Url url, final Object request) {
        Connection conn = connectionMgr.get(url);
        oneWay(conn, request);
    }



}
