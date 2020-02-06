package com.planet.staccato;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.config.StacConfigProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Runs the init method on all classes implementing {@link StacInitializer}.
 *
 * @author joshfix
 * Created on 1/18/18
 */
@Slf4j
@Component
public class StaccatoApplicationInitializer implements ApplicationRunner {

    @Autowired
    private StacConfigProps configProps;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaccatoHealthIndicator staccatoHealthIndicator;

    @Autowired(required = false)
    private List<StacInitializer> stacInitializers;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing Staccato");

        if (!configProps.isIncludeNullFields()) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        if (null != stacInitializers && !stacInitializers.isEmpty()) {
            for (StacInitializer stacInitializer : stacInitializers) {
                log.info("Initializer matched: " + stacInitializer.getName());
                stacInitializer.init();
            }
        }

        log.info("Staccato initialization complete. Setting health status to UP.");
        staccatoHealthIndicator.setHealthy(true);
    }
}
