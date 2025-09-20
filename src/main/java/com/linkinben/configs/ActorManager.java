package com.linkinben.configs;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.actor.typed.SpawnProtocol;
import com.linkinben.actors.GreetingActor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class ActorManager {
    private final ActorSystem<SpawnProtocol.Command> actorSystem;
    private final ActorRef<GreetingActor.Greet> greeterActor;

    public ActorManager() {
        this.actorSystem = ActorSystem.create(SpawnProtocol.create(), "akka-quarkus-system");
        this.greeterActor = actorSystem.systemActorOf(GreetingActor.create(), "greetingActor", Props.empty());
    }

    @Produces
    public ActorSystem<SpawnProtocol.Command> getActorSystem() {
        return actorSystem;
    }

    @Produces
    @Named("greetingActor")
    public ActorRef<GreetingActor.Greet> getGreeterActor() {
        return greeterActor;
    }

    public void destroyActorSystem(@Disposes ActorSystem<SpawnProtocol.Command> system) {
        system.terminate();
    }
}
