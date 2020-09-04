package io.andy.pigeon.net.core.message.invoker;


/**
 */
public interface OneWayInvoker<T> {

    /**
     * 远程执行
     * @param req
     * @return
     */
    void invoke(T req);


}
