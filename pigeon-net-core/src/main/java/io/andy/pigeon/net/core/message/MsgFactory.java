package io.andy.pigeon.net.core.message;


public interface MsgFactory {

    Envelope createOneWay(Object requestObject);

    Envelope createTwoWay(Object requestObject);

    Envelope createHeartbeatReq();

    Envelope createHeartbeatAck(Envelope req);

    <T extends Envelope> T createTwoWayAck(Envelope req, Object responseBody);

    <T extends Envelope> T createReqTimeout(Envelope req);

    <T extends Envelope> T createReqFailed(Envelope req, Object responseBody);
}
