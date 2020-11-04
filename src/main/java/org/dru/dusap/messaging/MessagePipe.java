package org.dru.dusap.messaging;

import java.util.function.Consumer;

public interface MessagePipe<T> extends MessageDispatcher<T> {
    boolean hasConsumer();

    void addConsumer(Consumer<? super T> consumer, MessageDeliverer deliverer);

    void addConsumer(Consumer<? super T> consumer);

    void removeConsumer(Consumer<? super T> consumer);
}
