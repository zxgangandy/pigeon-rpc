package io.andy.pigeon.net.core.message.invoker;

import lombok.Setter;

@Setter
public class DefaultInvokerMgr implements InvokerMgr{

    public static volatile DefaultInvokerMgr INSTANCE;

    private Invoker oneWayInvoker;

    private Invoker twoWayInvoker;

    public static DefaultInvokerMgr getInstance() {
        if ( null == INSTANCE) {
            synchronized (DefaultInvokerMgr.class) {
                if (null == INSTANCE) {
                    INSTANCE = new DefaultInvokerMgr();
                }
            }
        }

        return INSTANCE;
    }


    @Override
    public Invoker getOneWayInvoker() {
        return oneWayInvoker == null ? getDefaultOneWayInvoker() : oneWayInvoker;
    }

    @Override
    public Invoker getTwoWayInvoker() {
        return twoWayInvoker == null ? getDefaultTwoWayInvoker() : twoWayInvoker;
    }

}
