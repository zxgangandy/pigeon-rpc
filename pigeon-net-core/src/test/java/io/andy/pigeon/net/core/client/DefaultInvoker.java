package io.andy.pigeon.net.core.client;

import io.andy.pigeon.net.core.message.MsgContext;
import io.andy.pigeon.net.core.message.ReqMsg;
import io.andy.pigeon.net.core.message.RespMsg;
import io.andy.pigeon.net.core.message.invoker.Invoker;

public class DefaultInvoker implements Invoker<RespMsg> {
    @Override
    public RespMsg invoke(MsgContext context, ReqMsg req) {
        System.out.println("req"+req);
        return new RespMsg("tow way ACK");
    }
}
