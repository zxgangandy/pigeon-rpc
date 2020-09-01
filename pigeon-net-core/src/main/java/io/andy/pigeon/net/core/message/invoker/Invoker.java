package io.andy.pigeon.net.core.message.invoker;


/**
 */
public interface Invoker<T, R> {

    /**
     * 远程执行
     * @param req
     * @return
     */
    R invoke(T req);


}
