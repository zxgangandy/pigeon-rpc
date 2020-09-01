package io.andy.pigeon.net.core;


import io.andy.pigeon.net.core.exception.StartException;

import java.lang.reflect.Constructor;

public class Pigeon {
    private static volatile Pigeon INSTANCE;

    public static <T> T with(Class<T> clazz ) {
        return getDefault().build(clazz);
    }

    private static Pigeon getDefault() {
        if ( null == INSTANCE) {
            synchronized (Pigeon.class) {
                if (null == INSTANCE) {
                    INSTANCE = new Pigeon();
                }
            }
        }

        return INSTANCE;
    }

    public  <T> T build(Class<T> clazz)  {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new StartException(String.format("Start (%s) failed!", clazz.getSimpleName()));
        }
    }

}
