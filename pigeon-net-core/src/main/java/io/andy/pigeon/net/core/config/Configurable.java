package io.andy.pigeon.net.core.config;


public interface Configurable {
    <T> Configurable option(Option<T> option, T value);

    <T> T option(Option<T> option);
}
