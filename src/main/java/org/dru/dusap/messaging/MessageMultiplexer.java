package org.dru.dusap.messaging;

import java.util.Objects;

public final class MessageMultiplexer<T> implements MessageDispatcher<T> {
    private final MessageDispatcher<T> first;
    private final MessageDispatcher<T> second;

    public MessageMultiplexer(final MessageDispatcher<T> first, final MessageDispatcher<T> second) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        this.first = first;
        this.second = second;
    }

    @Override
    public void dispatchMessage(final T message) {
        first.dispatchMessage(message);
        second.dispatchMessage(message);
    }
}
