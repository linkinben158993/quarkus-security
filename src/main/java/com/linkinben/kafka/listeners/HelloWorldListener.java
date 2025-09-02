package com.linkinben.kafka.listeners;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@Slf4j
@ApplicationScoped
public class HelloWorldListener {

    @Incoming("data-input")
    @Outgoing("data-output")
    public Record<String, String> processMessage(Record<String, String> message) {
        log.info("Received a message with content key={}, value={}", message.key(), message.value());
        return message;
    }

}
