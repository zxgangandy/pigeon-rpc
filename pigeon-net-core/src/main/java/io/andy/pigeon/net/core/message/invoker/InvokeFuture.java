package io.andy.pigeon.net.core.message.invoker;


public interface InvokeFuture<V> {
    /**
     * Wait response with timeout.
     *
     * @param timeoutMillis time out in millisecond
     * @return the computed result
     * @throws InterruptedException if interrupted
     */
    V get(final long timeoutMillis) throws InterruptedException;

    /**
     * Wait response with unlimit timeout
     *
     * @return the computed result
     * @throws InterruptedException if interrupted
     */
    V get() throws InterruptedException;



    boolean isDone();


    void complete(V v);
}
