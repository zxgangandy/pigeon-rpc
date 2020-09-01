package io.andy.pigeon.net.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Url {
    /** ip, can be number format or hostname format*/
    private String     ip;

    /** port, should be integer between (0, 65535]*/
    private int        port;

    public Url(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
