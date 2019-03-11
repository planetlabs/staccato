package com.planet.staccato.grpc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author joshfix
 * Created on 2/14/19
 */
@Data
@ConfigurationProperties(prefix = "staccato.grpc")
public class GrpcConfigProps {
    private int port = 9999;
}

