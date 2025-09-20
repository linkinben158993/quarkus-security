package com.linkinben.actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GreetingActor extends AbstractBehavior<GreetingActor.Greet> {
    public GreetingActor(ActorContext<Greet> context) {
        super(context);
    }

    public static class Greet {
        public final String message;

        public Greet(String message) {
            this.message = message;
        }
    }

    public static Behavior<Greet> create() {
        return Behaviors.setup(GreetingActor::new);
    }

    @Override
    public Receive<Greet> createReceive() {
        return newReceiveBuilder()
                .onMessage(Greet.class, this::onGreet)
                .build();
    }


    private Behavior<Greet> onGreet(Greet greet) {
        log.info("Received a command with message: {}", greet.message);
        return this;
    }
}
