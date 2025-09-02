package com.linkinben;

import com.linkinben.gatling.AbstractAsyncScenario;
import com.linkinben.gatling.actions.KeyOnlyKafkaConsumer;
import com.linkinben.gatling.config.TestPlanLoader;
import com.linkinben.gatling.integrations.base.BaseReceiver;
import com.linkinben.gatling.integrations.base.BaseSender;
import com.linkinben.gatling.integrations.kafka.ConcreteKafkaConsumer;
import com.linkinben.gatling.integrations.kafka.ConcreteKafkaProducer;
import com.linkinben.gatling.integrations.kafka.KeyAndValuesKafkaConsumer;
import com.linkinben.gatling.model.ExecutionConfiguration;
import com.linkinben.gatling.simulation.BaseSimulation;
import com.linkinben.scenarios.ScenarioFactory;
import com.typesafe.config.Config;
import io.gatling.javaapi.core.Assertion;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.details;
import static io.gatling.javaapi.core.CoreDsl.global;


@Slf4j
public class SecuritySimulation extends BaseSimulation {
    private ConcreteKafkaProducer<String, String> producer;
    private KeyAndValuesKafkaConsumer<String, String> consumer;
    private ConcreteKafkaConsumer<String, String> keyConsumer;
    private TestPlanLoader<ExecutionConfiguration> testPlanLoader;

    public SecuritySimulation() throws Exception {
        testPlanLoader = new TestPlanLoader<>(ExecutionConfiguration.class);
        producer = new ConcreteKafkaProducer<>(config.getKafkaConsumerProperties());
        consumer = new KeyAndValuesKafkaConsumer<>(List.of(config.getKafkaTopic("output")), config.getKafkaConsumerProperties());
        keyConsumer = new KeyOnlyKafkaConsumer<>(config.getKafkaConsumerProperties(), List.of(config.getKafkaTopic("output")));

        var simulation = setUp(getScenario());

        if (config.isWarmup()) {
            simulation.assertions(
                    global().successfulRequests().percent().gt(90D)
            );
        } else {
            simulation.assertions(
                    addAssertions()
            );
        }
    }

    private List<Assertion> getSuccessAssertions(Config environmentConfig) {
        log.info("Environment config={}", environmentConfig);
        var assertions = new ArrayList<Assertion>();
        assertions.add(global().successfulRequests().percent().is(100D));


        return assertions;
    }

    private List<Assertion> getSlaAssertion(Config environmentConfig, String scenarioName) {
        var assertions = new ArrayList<Assertion>();
        var workflowSla = 60000;

        assertions.add(details(scenarioName).responseTime().percentile4().lte(workflowSla));
        return assertions;
    }

    private List<Assertion> addAssertions() {
        List<Assertion> assertions = new ArrayList<>(getSuccessAssertions(config.get()));

        for (var plan : testPlanLoader.getTestPlan().getPlans()) {
            for (var scenario : plan.getScenarios()) {
                if (!"Warmup".equalsIgnoreCase(scenario.getName())) {
                    assertions.addAll(getSlaAssertion(config.get(), scenario.getName()));
                }
            }
        }

        return assertions;
    }

    @Override
    protected @NonNull List<BaseReceiver> getReceivers() {
        return List.of(consumer);
    }

    @Override
    protected @NonNull List<BaseSender> getSenders() {
        return List.of(producer);
    }

    @Override
    protected Map<String, AbstractAsyncScenario> getScenarioMap() {
        ScenarioFactory scenarioFactory = ScenarioFactory.getInstance(config, producer, consumer, keyConsumer);

        return scenarioFactory.getScenarioMap();
    }
}