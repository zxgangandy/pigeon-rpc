package io.andy.pigeon.net.core.message.invoker;

public interface InvokerMgr {
    Invoker getOneWayInvoker();

    Invoker getTwoWayInvoker();

    default OneWayInvoker getDefaultOneWayInvoker() {
        return new OneWayInvoker();
    }

    default TwoWayInvoker getDefaultTwoWayInvoker() {
        return new TwoWayInvoker();
    }

}
