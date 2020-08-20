
package io.andy.pigeon.net.core.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread factory to name the thread purposely
 *
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber   = new AtomicInteger(1);
    private final AtomicInteger        threadNumber = new AtomicInteger(1);
    private final ThreadGroup          group;
    private final String               namePrefix;
    private final boolean              isDaemon;

    public NamedThreadFactory() {
        this("ThreadPool");
    }

    public NamedThreadFactory(String name) {
        this(name, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-thread-";
        isDaemon = daemon;
    }

    /**
     * Create a thread.
     *
     * @see ThreadFactory#newThread(Runnable)
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        t.setDaemon(isDaemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

}
