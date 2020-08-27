package io.andy.pigeon.net.core.connection.event.listener;

import io.andy.pigeon.net.core.connection.Connection;
import io.andy.pigeon.net.core.connection.ConnectionEvent;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EventListenerMgr {

    private ConcurrentHashMap<ConnectionEvent, List<EventListener>> listeners = new ConcurrentHashMap<>(1);

    private static volatile EventListenerMgr INSTANCE;

    public static EventListenerMgr getInstance() {
        if (null == INSTANCE) {
            synchronized (EventListenerMgr.class) {
                if (null == INSTANCE) {
                    INSTANCE = new EventListenerMgr();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * Dispatch events.
     *
     * @param type ConnectionEvent
     * @param connection Connection
     */
    public void onEvent(ConnectionEvent type, Connection connection) {
        List<EventListener> listenerList = this.listeners.get(type);
        if (listenerList != null && !CollectionUtils.isEmpty(listenerList)) {
            for (EventListener processor : listenerList) {
                processor.onEvent(connection);
            }
        }
    }

    /**
     * Add event listener.
     *
     * @param type ConnectionEvent
     * @param listener ConnectionEventListener
     */
    public void addEventListener(ConnectionEvent type, EventListener listener) {
        List<EventListener> listenerList = this.listeners.get(type);
        if (listenerList == null) {
            this.listeners.putIfAbsent(type, new ArrayList<>(1));
            listenerList = this.listeners.get(type);
        }
        listenerList.add(listener);
    }

}
