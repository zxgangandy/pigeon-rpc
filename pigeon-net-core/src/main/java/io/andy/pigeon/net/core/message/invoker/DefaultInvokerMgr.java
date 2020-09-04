package io.andy.pigeon.net.core.message.invoker;

import lombok.Setter;

@Setter
public class DefaultInvokerMgr implements InvokerMgr{

    public static volatile DefaultInvokerMgr INSTANCE;

    private Invoker invoker;

    public static DefaultInvokerMgr getInstance() {
        if (null == INSTANCE) {
            synchronized (DefaultInvokerMgr.class) {
                if (null == INSTANCE) {
                    INSTANCE = new DefaultInvokerMgr();
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public Invoker getInvoker() {
        return invoker;
    }


}
