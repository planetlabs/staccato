package com.planet.staccato.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * General configuration for various STAC options.
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Data
@Component
@ConfigurationProperties(prefix = "staccato")
public class StacConfigProps {

    private String version;
    private boolean includeNullFields = false;
    private boolean generateSelfLinks = true;
    private boolean generateThumbnailLinks = true;
}