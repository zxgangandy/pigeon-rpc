package io.andy.pigeon.net.core.message;

import lombok.Data;

@Data
public class RespMsg extends MsgEnvelope {
    private Object response;

    public RespMsg(Object response) {
        this.response = response;
    }

}
