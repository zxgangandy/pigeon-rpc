package io.andy.pigeon.net.core.config;


public class ServerOption<T> extends BaseOption<T> {

    public static final Option<Integer> PORT = valueOf("netty.server.port", 8500);

    protected ServerOption(String name, T defaultValue) {
        super(name, defaultValue);
    }


}
