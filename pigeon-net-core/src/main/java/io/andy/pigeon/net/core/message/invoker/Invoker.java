package io.andy.pigeon.net.core.message.invoker;


import io.andy.pigeon.net.core.message.MsgContext;
import io.andy.pigeon.net.core.message.ReqMsg;

/**
 *  Invoke functions after received peer's message.
 */
public interface Invoker<R> {

    R invoke(MsgContext context, ReqMsg req);
}
