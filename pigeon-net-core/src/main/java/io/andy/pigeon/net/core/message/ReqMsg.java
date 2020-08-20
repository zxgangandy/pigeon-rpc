package io.andy.pigeon.net.core.message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReqMsg extends MsgEnvelope {
    private Object request;

    public ReqMsg(Object request) {
        this.request = request;
    }
}
