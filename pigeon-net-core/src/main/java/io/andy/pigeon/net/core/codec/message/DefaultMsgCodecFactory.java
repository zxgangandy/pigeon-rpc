package io.andy.pigeon.net.core.codec.message;

public class DefaultMsgCodecFactory implements MsgCodecFactory {
    @Override
    public MsgEncoder getEncoder() {
        return new DefaultMsgEncoder();
    }

    @Override
    public MsgDecoder getDecoder() {
        return new DefaultMsgDecoder(new DefaultMsgHeaderDecoder());
    }
}
