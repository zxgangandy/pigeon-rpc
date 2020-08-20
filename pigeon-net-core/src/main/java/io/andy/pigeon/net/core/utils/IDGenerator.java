package io.andy.pigeon.net.core.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {
    private static final AtomicLong id = new AtomicLong();

    /**
     * generate the next id
     *
     * @return
     */
    public static long nextId() {
        return id.incrementAndGet();
    }
}
