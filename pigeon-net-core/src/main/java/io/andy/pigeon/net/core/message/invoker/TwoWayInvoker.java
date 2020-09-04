package io.andy.pigeon.net.core.message.invoker;


import io.andy.pigeon.net.core.connection.Connection;

/**
 */
public interface TwoWayInvoker<T, R> {

    /**
     * 远程执行
     * @param req
     * @return
     */
    R invoke(Connection connection,  T req);


}
