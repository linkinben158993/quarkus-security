package com.linkinben.kafka.listeners;

import akka.actor.typed.ActorRef;
import com.linkinben.actors.GreetingActor;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class HelloWorldListener {
    @Inject
    @Named
    private final ActorRef<GreetingActor.Greet> greetingActor;

    @Incoming("data-input")
    @Outgoing("data-output")
    public Record<String, String> processMessage(Record<String, String> message) {
        log.info("Received a message with content key={}, value={}", message.key(), message.value());
        greetingActor.tell(new GreetingActor.Greet("Hello from key=" + message.key() + ", value=" + message.value()));
        return message;
    }

}
