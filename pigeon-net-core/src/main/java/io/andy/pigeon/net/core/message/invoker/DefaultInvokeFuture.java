package io.andy.pigeon.net.core.message.invoker;

import io.andy.pigeon.net.core.message.Envelope;
import io.netty.util.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DefaultInvokeFuture implements InvokeFuture {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private volatile Envelope responseEnvelope;

    private long invokeId;

    private Timeout timeout;



    public DefaultInvokeFuture(long invokeId) {
        this.invokeId = invokeId;
    }

    @Override
    public <T> T get(long timeoutMillis) throws InterruptedException {
        this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return (T) this.responseEnvelope;
    }

    @Override
    public <T> T get() throws InterruptedException {
        this.countDownLatch.await();
        return (T) this.responseEnvelope;
    }

    @Override
    public boolean isDone() {
        return this.countDownLatch.getCount() <= 0;
    }

    @Override
    public <T> void complete(T envelope) {
        this.responseEnvelope = (Envelope) envelope;
        this.countDownLatch.countDown();
    }

    @Override
    public long invokeId() {
        return invokeId;
    }

    @Override
    public void addTimeout(Timeout timeout) {
        this.timeout = timeout;
    }

    @Override
    public void cancelTimeout() {
        if (this.timeout != null) {
            this.timeout.cancel();
        }
    }


}
