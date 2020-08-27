
package io.andy.pigeon.net.core.utils;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

/**
 * A singleton holder of the timer for timeout.
 *
 */
public class TimerHolder {

    private final static long defaultTickDuration = 10;

    private static class DefaultInstance {
        static final Timer INSTANCE = new HashedWheelTimer(new NamedThreadFactory(
                                        "default-timer-thread" + defaultTickDuration, true),
                                        defaultTickDuration, TimeUnit.MILLISECONDS);
    }

    private TimerHolder() {
    }

    /**
     * Get a singleton instance of {@link Timer}. <br>
     * The tick duration is {@link #defaultTickDuration}.
     *
     * @return Timer
     */
    public static Timer getTimer() {
        return DefaultInstance.INSTANCE;
    }
}
