# dusap-messaging
### Messaging made pretty simple
```java
        // Single type messaging
        MessagePipe<Object> myPipe = new DefaultMessagePipe<>();
        myPipe.addConsumer(msg -> System.out.println("myPipe: " + msg), DefaultMessageDeliverer.INSTANCE);
        myPipe.dispatchMessage("hey");
        myPipe.dispatchMessage(1);

        // Multi type messaging
        MessageBus myBus = new DefaultMessageBus();
        myBus.addConsumer(String.class, msg -> System.out.println("myBus<string>: " + msg),
                DefaultMessageDeliverer.INSTANCE);
        myBus.addConsumer(Integer.class, msg -> System.out.println("myBus<int>: " + msg),
                DefaultMessageDeliverer.INSTANCE);
        myBus.addConsumer(Float.class, msg -> System.out.println("myBus<float>: " + msg),
                DefaultMessageDeliverer.INSTANCE);
        myBus.dispatchMessage("hey");
        myBus.dispatchMessage(1);
        myBus.dispatchMessage(1f);

        // Multiplexing
        MessageDispatcher<Object> myMultiplexer = new MessageMultiplexer<>(myBus, myPipe);
        myMultiplexer.dispatchMessage("hey");
        myMultiplexer.dispatchMessage(1);
        myMultiplexer.dispatchMessage(1f);
```
Implement your own MessageDeliverer, example below is to have the messages delivered in the swing ui thread.
```java
enum SwingMessageDeliverer implements MessageDeliverer {
    INSTANCE;

    @Override
    public <T> void deliverMessage(final T message, final Collection<Consumer<? super T>> consumers) {
        SwingUtilities.invokeLater(() -> DefaultMessageDeliverer.INSTANCE.deliverMessage(message, consumers));
    }
}
```