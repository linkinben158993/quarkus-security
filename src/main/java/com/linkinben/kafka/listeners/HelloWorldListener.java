package com.linkinben.kafka.listeners;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
public class HelloWorldListener {

    @Incoming("data-inpnut")
    public void processMessage(String message) {
        log.info("Received a message with content={}", message);
    }

}
