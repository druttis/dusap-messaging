package org.dru.dusap.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.function.Consumer;

public enum DefaultMessageDeliverer implements MessageDeliverer {
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public <T> void deliverMessage(final T message, final Collection<Consumer<? super T>> consumers) {
        consumers.forEach(consumer -> {
            try {
                consumer.accept(message);
            } catch (final RuntimeException exc) {
                logger.error("unhandled runtime-exception caught", exc);
            }
        });
    }
}
