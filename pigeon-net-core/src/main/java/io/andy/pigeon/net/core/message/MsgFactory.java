package io.andy.pigeon.net.core.message;

public interface MsgFactory {

    Envelope createOneWay(Object requestObject);

    Envelope createTwoWay(Object requestObject);

    Envelope createHeartbeatReq();

    Envelope createHeartbeatAck(Envelope req);

    Envelope createTwoWayAck(Envelope req, Object responseBody);
}
