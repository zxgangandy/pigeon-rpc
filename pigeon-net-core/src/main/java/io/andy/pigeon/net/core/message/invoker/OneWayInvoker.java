package io.andy.pigeon.net.core.message.invoker;

import io.andy.pigeon.net.core.message.ReqMsg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OneWayInvoker implements Invoker<ReqMsg, Void> {

    @Override
    public Void invoke(ReqMsg req) {
        log.info("req invoke, req={}", req);
        return null;
    }
}
