package org.dru.dusap.messaging;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class DefaultMessagePipe<T> implements MessagePipe<T> {
    private final Map<MessageDeliverer, List<Consumer<? super T>>> consumersByDeliverer;

    public DefaultMessagePipe() {
        consumersByDeliverer = new ConcurrentHashMap<>();
    }

    @Override
    public boolean hasConsumer() {
        return !consumersByDeliverer.isEmpty();
    }

    @Override
    public void addConsumer(final Consumer<? super T> consumer, final MessageDeliverer deliverer) {
        Objects.requireNonNull(consumer, "consumer");
        Objects.requireNonNull(deliverer, "deliverer");
        addConsumerUnchecked(consumer, deliverer);
    }

    @Override
    public void addConsumer(final Consumer<? super T> consumer) {
        addConsumer(consumer, DefaultMessageDeliverer.INSTANCE);
    }

    @Override
    public void removeConsumer(final Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        removeConsumerUnchecked(consumer);
    }

    @Override
    public void dispatchMessage(final T message) {
        consumersByDeliverer.forEach((deliverer, consumers) -> deliverer.deliverMessage(message, consumers));
    }

    public void addConsumerUnchecked(final Consumer<? super T> consumer, final MessageDeliverer deliverer) {
        consumersByDeliverer.computeIfAbsent(deliverer, $ -> new CopyOnWriteArrayList<>()).add(consumer);
    }

    public void removeConsumerUnchecked(final Consumer<? super T> consumer) {
        consumersByDeliverer.values().removeIf(list -> list.remove(consumer) && list.isEmpty());
    }
}
