package io.andy.pigeon.net.core.base;

import io.andy.pigeon.net.core.config.Configurable;
import io.andy.pigeon.net.core.config.Option;
import io.andy.pigeon.net.core.config.Options;
import io.netty.util.internal.ObjectUtil;

public abstract class AbstractRemotingEndpoint implements RemotingEndpoint, Configurable {

    private final Options options = new Options();

    @Override
    public <T> Configurable option(Option<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        this.options.option(option, value);
        return this;
    }

    @Override
    public <T> T option(Option<T> option) {
        return options.option(option);
    }


}
