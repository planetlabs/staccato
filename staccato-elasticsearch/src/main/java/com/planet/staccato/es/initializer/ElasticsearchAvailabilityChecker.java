package com.planet.staccato.es.initializer;

import com.planet.staccato.StacInitializer;
import com.planet.staccato.es.config.ElasticsearchConfigProps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * An initializer that will prevent the STAC application from reporting a healthy status until it can successfully
 * connect to the Elasticsearch instance and retrieve the version number or until the maximum number of attempts
 * as defined in {@link ElasticsearchConfigProps ElasticsearchConfigProps.Es.maxReconnectionAttempts} has been reached.
 * After a failed attempt, reattempts will implement a back-off delay strategy according the the fibonacci sequence.
 * @author joshfix
 * Created on 2/27/18
 */
@Slf4j
@Component
@AllArgsConstructor
public class ElasticsearchAvailabilityChecker implements StacInitializer {

    private final ElasticsearchConfigProps configProps;
    private final RestHighLevelClient client;
    private final ApplicationContext appContext;

    @Override
    public void init() {
        int attempt = 0;
        while (attempt < configProps.getMaxReconnectionAttempts()) {

            try {
                MainResponse response = client.info();
                if (response.isAvailable()) {
                    log.info("Elasticsearch is available running version " + response.getVersion() + ".");
                    return;
                }
            } catch (IOException e) {
                // do nothing
            }

            try {
                int backoffPeriod = fibonacci(attempt++);
                log.warn("Elasticsearch is not available. Attempt " + attempt + ". Waiting " + backoffPeriod
                        + " seconds before reattempting connection.");
                TimeUnit.SECONDS.sleep(backoffPeriod);
            } catch (InterruptedException e) {
                // do nothing -- just move on to next attempt
            }
        }

        shutdown();
    }

    private int fibonacci(int n) {
        return n <= 1 ? n : fibonacci(n-1) + fibonacci(n-2);
    }

    private void shutdown() {
        log.error("Elasticsearch is not available. Max retry attempts ("
                + configProps.getMaxReconnectionAttempts() + ") has been reached. Shutting down...");
        SpringApplication.exit(appContext, () -> 1);
        System.exit(1);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

}
