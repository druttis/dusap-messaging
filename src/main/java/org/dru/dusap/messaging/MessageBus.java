package org.dru.dusap.messaging;

import java.util.function.Consumer;

public interface MessageBus extends MessageDispatcher<Object> {
    boolean hasConsumer();

    boolean hasConsumer(Class<?> type);

    <T> void addConsumer(Class<T> type, Consumer<? super T> consumer, MessageDeliverer deliverer);

    <T> void addConsumer(Class<T> type, Consumer<? super T> consumer);

    <T> void removeConsumer(Class<T> type, Consumer<? super T> consumer);
}
