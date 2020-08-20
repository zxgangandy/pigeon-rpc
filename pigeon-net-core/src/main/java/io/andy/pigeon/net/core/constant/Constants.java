package io.andy.pigeon.net.core.constant;

public class Constants {

    /**
     * 服务端默认读超时时间
     */
    public static final int SERVER_READER_IDLE_TIME_SECONDS = 0;

    /**
     * 服务端默认写超时时间
     */
    public static final int SERVER_WRITER_IDLE_TIME_SECONDS = 0;

    /**
     * 服务端默认超时时间
     */
    public static final int SERVER_ALL_IDLE_TIME_SECONDS = 60;


    /**
     * 客户端默认读超时时间
     */
    public static final int CLIENT_READER_IDLE_TIME_SECONDS = 60;

    /**
     * 客户端默认写超时时间
     */
    public static final int CLIENT_WRITER_IDLE_TIME_SECONDS = 60;

    /**
     * 客户端默认超时时间
     */
    public static final int CLIENT_ALL_IDLE_TIME_SECONDS = 0;


    public static final String MAX_CONNECTION = "max.connection";
    public static final int DEFAULT_MAX_CONNECTION = 1;
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
}
