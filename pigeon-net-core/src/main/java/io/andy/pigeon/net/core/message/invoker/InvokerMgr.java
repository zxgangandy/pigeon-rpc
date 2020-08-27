package io.andy.pigeon.net.core.message.invoker;

public interface InvokerMgr {
    Invoker getReqInvoker();

    Invoker getRespInvoker();

    default DefaultReqInvoker getDefaultReqInvoker() {
        return new DefaultReqInvoker();
    }

    default DefaultRespInvoker getDefaultRespInvoker() {
        return new DefaultRespInvoker();
    }
}
