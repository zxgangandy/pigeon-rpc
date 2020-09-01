package io.andy.pigeon.net.core.config;

import static io.andy.pigeon.net.core.constant.Constants.*;

public class ClientOption<T> extends BaseOption<T> {

    public static final Option<Integer> MAX_CONNS = valueOf(ClientOption.class, "client.max.connection", DEFAULT_MAX_CONNECTION);
    public static final Option<Integer> CONNECT_TIMEOUT = valueOf(ClientOption.class, "client.connect.timeout", DEFAULT_CONNECT_TIMEOUT);
    public static final Option<Integer> REQUEST_TIMEOUT = valueOf(ClientOption.class, "client.request.timeout", DEFAULT_REQUEST_TIMEOUT);

    protected ClientOption(String name, T defaultValue) {
        super(name, defaultValue);
    }


}
