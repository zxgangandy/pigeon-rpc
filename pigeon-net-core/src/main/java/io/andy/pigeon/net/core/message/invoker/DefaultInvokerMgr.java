package io.andy.pigeon.net.core.message.invoker;

import lombok.Setter;

@Setter
public class DefaultInvokerMgr implements InvokerMgr{

    public static volatile DefaultInvokerMgr INSTANCE;

    private Invoker reqInvoker;
    private Invoker respInvoker;

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
    public Invoker getReqInvoker() {
        return reqInvoker == null ? getDefaultReqInvoker() : reqInvoker;
    }

    @Override
    public Invoker getRespInvoker() {
        return respInvoker == null ? getDefaultRespInvoker() : respInvoker;
    }
}
