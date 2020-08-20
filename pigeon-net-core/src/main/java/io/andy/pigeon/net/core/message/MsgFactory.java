package io.andy.pigeon.net.core.message;

public interface MsgFactory {


    Envelope createOneWay(final Object requestObject);


    Envelope createTwoWay(final Object requestObject);
}
