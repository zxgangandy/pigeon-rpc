package io.andy.pigeon.net.core.message.dispatcher;

import io.andy.pigeon.net.core.message.Envelope;
import io.andy.pigeon.net.core.message.processor.DefaultMsgProcessor;
import io.andy.pigeon.net.core.message.processor.MsgProcessor;
import io.andy.pigeon.net.core.utils.NamedThreadFactory;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultMsgDispatcher implements MsgDispatcher {
    private boolean serverSide;
    private MsgProcessor msgProcessor;
    private ExecutorService executor;

    public DefaultMsgDispatcher(boolean serverSide) {
        this.serverSide = serverSide;
        this.msgProcessor = new DefaultMsgProcessor();
        this.executor = new ThreadPoolExecutor(8,
                16,
                20,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(60),
                new NamedThreadFactory("Message-dispatcher-executor", true));
    }

    @Override
    public void dispatch(final ChannelHandlerContext ctx, Envelope msg) {
        executor.execute(()->{
            msgProcessor.process(ctx, msg);
        });
    }
}
