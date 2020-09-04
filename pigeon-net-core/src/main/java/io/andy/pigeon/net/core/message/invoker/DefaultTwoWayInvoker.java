package io.andy.pigeon.net.core.message.invoker;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.message.DefaultMsgFactory;
import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.ReqMsg;
import io.andy.pigeon.net.core.message.RespMsg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultTwoWayInvoker implements TwoWayInvoker<ReqMsg, RespMsg> {


    @Override
    public RespMsg invoke(Connection connection,  ReqMsg req) {
        log.info("req invoke, req={}", req);
        Envelope respMsg = DefaultMsgFactory.getInstance().createTwoWayAck(req, "hello");
        return (RespMsg) respMsg;
    }
}
