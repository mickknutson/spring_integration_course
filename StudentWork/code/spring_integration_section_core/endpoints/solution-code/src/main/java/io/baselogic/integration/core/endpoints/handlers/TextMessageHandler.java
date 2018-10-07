package io.baselogic.integration.core.endpoints.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.MessageRejectedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class TextMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        Object payload = message.getPayload();

        if (payload instanceof String) {

            receiveAndAcknowledge((String) payload);

        } else {
            throw new MessageRejectedException(message, "Unknown data type has been received.");
        }
    }

    void receiveAndAcknowledge(String message) {
        log.info("******************************************");
        log.info("Message received successfully: {}", message);
    }

} // The End...
