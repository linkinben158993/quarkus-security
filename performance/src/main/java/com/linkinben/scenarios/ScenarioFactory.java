package com.linkinben.scenarios;

import com.linkinben.gatling.AbstractAsyncScenario;
import com.linkinben.gatling.config.EnvironmentConfigLoader;
import com.linkinben.gatling.integrations.kafka.ConcreteKafkaProducer;
import com.linkinben.gatling.integrations.kafka.KeyAndValuesKafkaConsumer;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

@Getter
public class ScenarioFactory {
    private final Map<String, AbstractAsyncScenario> scenarioMap;
    private static ScenarioFactory instance;

    private ScenarioFactory(EnvironmentConfigLoader config,
                            ConcreteKafkaProducer<String, String> producer,
                            KeyAndValuesKafkaConsumer<String, String> consumer) {
        scenarioMap = Map.ofEntries(
                entry("SecurityInputScenario", new SecurityInputScenario("SecurityInputScenario", config, consumer, producer))
        );
    }

    public static ScenarioFactory getInstance(EnvironmentConfigLoader config,
                                       ConcreteKafkaProducer<String, String> producer,
                                              KeyAndValuesKafkaConsumer<String, String> consumer) {
        if (Objects.isNull(instance)) {
            instance = new ScenarioFactory(config,
                    producer, consumer);
        }

        return instance;
    }
}
