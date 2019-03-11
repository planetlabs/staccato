package com.planet.staccato;

import lombok.Data;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * This class provides a custom health indicator for Spring Actuator.  The health status will only change to up after
 * the {@link StaccatoApplicationInitializer} has completed running all initializations which prevents environments from
 * using this instance until it has been fully initialized.
 *
 * @author joshfix
 * Created on 2/27/18
 */
@Data
@Component
public class StaccatoHealthIndicator implements HealthIndicator {

    private boolean healthy = false;

    @Override
    public Health health() {
        if (healthy) {
            return Health.up().build();
        }
        return Health.down().build();
    }
}
