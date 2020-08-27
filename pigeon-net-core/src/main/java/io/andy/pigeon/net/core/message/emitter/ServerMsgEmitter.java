package io.andy.pigeon.net.core.message.emitter;

import io.andy.pigeon.net.core.base.Url;
import io.andy.pigeon.net.core.connection.ConnectionMgr;

public class ServerMsgEmitter extends BaseMsgEmitter {
    public ServerMsgEmitter(ConnectionMgr connectionMgr) {
        super(connectionMgr);
    }

    @Override
    public void oneWay(Url url, Object request) {

    }
}
