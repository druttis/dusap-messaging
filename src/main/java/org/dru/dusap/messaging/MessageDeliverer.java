package org.dru.dusap.messaging;

import java.util.Collection;
import java.util.function.Consumer;

public interface MessageDeliverer {
    <T> void deliverMessage(T message, Collection<Consumer<? super T>> consumers);
}
