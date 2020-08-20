package io.andy.pigeon.net.core.message;

public class RespMsg extends MsgEnvelope {
    private Object response;

    public RespMsg(Object response) {
        this.response = response;
    }

}
