
package io.andy.pigeon.net.core.message;


import io.andy.pigeon.net.core.connection.Connection;

/**
 * Wrap the ChannelHandlerContext.
 *
 */
public class MsgContext {

    private boolean                                     serverSide     = false;

    /** whether need handle request timeout, if true, request will be discarded. The default value is true */
    private boolean                                     timeoutDiscard = true;

    /** request arrive time stamp */
    private long                                        arriveTimestamp;

    /** request timeout setting by invoke side */
    private int                                         timeout;

    /** message type */
    private MsgType                                     msgType;

    private Connection                                  connection;


    /**
     * Constructor.
     * @param serverSide
     * @param connection
     */
    public MsgContext(boolean serverSide, Connection connection) {
        this.serverSide = serverSide;
        this.connection = connection;
    }

    /**
     * The server side
     *
     * @return
     */
    public boolean isServerSide() {
        return this.serverSide;
    }

    /**
     * Setter method for property <tt>arriveTimestamp<tt>.
     *
     * @param arriveTimestamp value to be assigned to property arriveTimestamp
     */
    public void setArriveTimestamp(long arriveTimestamp) {
        this.arriveTimestamp = arriveTimestamp;
    }

    /**
     * Getter method for property <tt>arriveTimestamp</tt>.
     *
     * @return property value of arriveTimestamp
     */
    public long getArriveTimestamp() {
        return arriveTimestamp;
    }

    /**
     * Setter method for property <tt>timeout<tt>.
     *
     * @param timeout value to be assigned to property timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Getter method for property <tt>timeout</tt>.
     *
     * @return property value of timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Setter method for property <tt>msgType<tt>.
     *
     * @param msgType value to be assigned to msgType
     */
    public void setmsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public boolean isTimeoutDiscard() {
        return timeoutDiscard;
    }

    public Connection getConnection() {
        return connection;
    }
}
