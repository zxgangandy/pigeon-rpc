package io.andy.pigeon.net.core;


import io.andy.pigeon.net.core.config.Option;

public interface RemotingEndpoint {

    RemotingEndpoint start();

    boolean started();

    <T> RemotingEndpoint config(Option<T> option, T value);

    <T> T putOptionGet(Option<T> option);

    void stop();
}
