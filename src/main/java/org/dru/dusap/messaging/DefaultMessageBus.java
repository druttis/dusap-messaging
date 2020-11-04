package org.dru.dusap.messaging;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class DefaultMessageBus implements MessageBus {
    private final Map<Class<?>, MessagePipe<Object>> messagePipeByType;

    public DefaultMessageBus() {
        messagePipeByType = new ConcurrentHashMap<>();
    }

    @Override
    public boolean hasConsumer() {
        return !messagePipeByType.isEmpty();
    }

    @Override
    public boolean hasConsumer(final Class<?> type) {
        Objects.requireNonNull(type, "type");
        return messagePipeByType.containsKey(type);
    }

    @Override
    public <T> void addConsumer(final Class<T> type, final Consumer<? super T> consumer,
                                final MessageDeliverer deliverer) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(consumer, "consumer");
        Objects.requireNonNull(deliverer, "deliverer");
        ((DefaultMessagePipe<T>) messagePipeByType.computeIfAbsent(type, $ -> new DefaultMessagePipe<>()))
                .addConsumerUnchecked(consumer, deliverer);
    }

    @Override
    public <T> void addConsumer(final Class<T> type, final Consumer<? super T> consumer) {
        addConsumer(type, consumer, DefaultMessageDeliverer.INSTANCE);
    }

    @Override
    public <T> void removeConsumer(final Class<T> type, final Consumer<? super T> consumer) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(consumer, "consumer");
        messagePipeByType.computeIfPresent(type, ($, messagePipe) -> {
            ((DefaultMessagePipe<T>) messagePipe).removeConsumerUnchecked(consumer);
            return (messagePipe.hasConsumer() ? messagePipe : null);
        });
    }

    @Override
    public void dispatchMessage(final Object message) {
        Objects.requireNonNull(message, "message");
        final Class<?> type = message.getClass();
        final MessagePipe<Object> messagePipe = messagePipeByType.get(type);
        if (messagePipe != null) {
            messagePipe.dispatchMessage(message);
        }
    }
}
