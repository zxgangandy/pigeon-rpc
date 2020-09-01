package io.andy.pigeon.net.core.message.invoker;


public interface InvokeFuture {
    /**
     * Wait response with timeout.
     *
     * @param timeoutMillis time out in millisecond
     * @return the computed result
     * @throws InterruptedException if interrupted
     */
    <V> V get(final long timeoutMillis) throws InterruptedException;

    /**
     * Wait response with unlimit timeout
     *
     * @return the computed result
     * @throws InterruptedException if interrupted
     */
    <V> V get() throws InterruptedException;



    boolean isDone();


    <V> void complete(V v);

    long invokeId();
}
