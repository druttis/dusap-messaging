package org.dru.dusap.messaging;

public interface MessageDispatcher<T> {
    void dispatchMessage(T message);
}
