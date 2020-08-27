package io.andy.pigeon.net.core.message.invoker;

import io.andy.pigeon.net.core.message.Envelope;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DefaultInvokeFuture implements InvokeFuture<Envelope> {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private volatile Envelope responseEnvelope;

    public DefaultInvokeFuture() {

    }

    @Override
    public Envelope get(long timeoutMillis) throws InterruptedException {
        this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return this.responseEnvelope;
    }

    @Override
    public Envelope get() throws InterruptedException {
        this.countDownLatch.await();
        return this.responseEnvelope;
    }

    @Override
    public boolean isDone() {
        return this.countDownLatch.getCount() <= 0;
    }

    @Override
    public void complete(Envelope envelope) {
        this.responseEnvelope = envelope;
        this.countDownLatch.countDown();
    }


}
