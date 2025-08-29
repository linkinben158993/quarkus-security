package com.linkinben.scenarios;

import com.linkinben.gatling.config.EnvironmentConfigLoader;
import com.linkinben.gatling.integrations.kafka.ConcreteKafkaProducer;
import com.linkinben.gatling.integrations.kafka.KeyAndValuesKafkaConsumer;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

public class SecurityInputScenario extends SecurityTopicScenario {
    protected SecurityInputScenario(String scenarioName, EnvironmentConfigLoader config, KeyAndValuesKafkaConsumer<String, String> consumer, ConcreteKafkaProducer<String, String> producer) {
        super(scenarioName, config, consumer, producer);
    }

    @Override
    Function<String, String> getMessageValue() throws IOException {
        return
                prefix -> prefix + UUID.randomUUID();
    }
}
