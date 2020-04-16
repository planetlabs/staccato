package com.planet.staccato.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Configuration used for building dynamic links to include the protocol, host, port, and context path.
 *
 * @author joshfix
 * @author tingold
 */
@Data
@Component
@ConfigurationProperties(prefix = "staccato.links")
public class LinksConfigProps {

    private Self self = new Self();
    private Thumbnails thumbnails = new Thumbnails();

    public static String LINK_PREFIX;
    public static String LINK_BASE;

    @PostConstruct
    public void init() {
        LINK_PREFIX =
                getSelf().getScheme() + "://" + getSelf().getHost();

        if (getSelf().getPort() != 80) {
            LINK_PREFIX += ":" + getSelf().getPort();
        }

        LINK_BASE = LINK_PREFIX + "/search"; // generate the base string for links
    }

    @Data
    public static class Self {
        private String scheme = "http";
        private String host = "localhost";
        private Integer port = 8080;
        private String contextPath = "/";
    }

    @Data
    public static class Thumbnails {
        private String scheme = "http";
        private String host = "localhost";
        private Integer port = 8080;
        private String contextPath = "/thumbs";
    }

}
