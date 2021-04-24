package com.planet.staccato.config;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;

/**
 * TODO Currently unused.  Determine if Micrometer is worth supporting.  Likely needs to be an optional plugin.
 *
 * @author joshfix
 * Created on 10/22/18
 */
//@Component
//@Configuration
public class MicrometerConfig {

    @Bean
    public SimpleMeterRegistry simpleMeterRegistry() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();

        return registry;
    }
}
