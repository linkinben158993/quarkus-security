package com.linkinben.scenarios;

import com.linkinben.gatling.AbstractAsyncScenario;
import com.linkinben.gatling.config.EnvironmentConfigLoader;
import com.linkinben.gatling.integrations.kafka.ConcreteKafkaConsumer;
import com.linkinben.gatling.integrations.kafka.ConcreteKafkaProducer;
import com.linkinben.gatling.integrations.kafka.KeyAndValuesKafkaConsumer;
import io.gatling.javaapi.core.ScenarioBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.linkinben.gatling.actions.KafkaToKafkaDynamicKeyActionV2Dsl.kafkaToKafkaCustomKeyActionV2;
import static com.linkinben.gatling.actions.KafkaToKafkaActionDsl.kafkaToKafka;
import static io.gatling.javaapi.core.CoreDsl.scenario;

@Slf4j
public abstract class SecurityTopicScenario extends AbstractAsyncScenario {
    private final KeyAndValuesKafkaConsumer<String, String> consumer;
    private final ConcreteKafkaConsumer<String, String> keyConsumer;
    private final ConcreteKafkaProducer<String, String> producer;

    protected SecurityTopicScenario(String scenarioName, EnvironmentConfigLoader config, KeyAndValuesKafkaConsumer<String, String> consumer, ConcreteKafkaConsumer<String, String> keyConsumer, ConcreteKafkaProducer<String, String> producer) {
        super(scenarioName, config);
        this.consumer = consumer;
        this.keyConsumer = keyConsumer;
        this.producer = producer;
    }

    abstract Function<String, String> getMessageValue() throws IOException;

    protected Supplier<String> getUuid() {
        return () -> UUID.randomUUID().toString();
    }

    protected String getInputTopic() {
        return config.getKafkaTopic("input");
    }

    @Override
    public ScenarioBuilder getScenario(boolean warmup) {
        try {
            return scenario(scenarioName)
                    .exec(kafkaToKafka(String.class, String.class)
                            .executor(getExecutor())
                            .name(scenarioName)
                            .kafkaProducer(producer)
                            .kafkaConsumer(keyConsumer)
                            .inputTopic(getInputTopic())
                            .valueSupplier(getMessageValue())
//                            .keyExtractor(key -> key)
                            .keySupplier(getUuid())
                            .maxTimeoutMs(Long.valueOf("30000"))
                            .build());

        } catch (IOException e) {
            log.error("Sumthing wong", e);
            return null;
        }
    }
}
