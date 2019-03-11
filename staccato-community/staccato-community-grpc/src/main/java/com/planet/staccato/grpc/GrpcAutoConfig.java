package com.planet.staccato.grpc;

import com.salesforce.grpc.contrib.spring.GrpcServerHost;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author joshfix
 * Created on 2/14/19
 */
@Slf4j
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "staccato.grpc", value = "enabled", havingValue = "true")
public class GrpcAutoConfig {

    @Bean(initMethod = "start")
    public GrpcServerHost grpcServerHost() {
        int port = grpcConfigProps().getPort();
        log.info("Listening for gRPC on port " + port);
        return new GrpcServerHost(port);
    }

    @Bean
    public GrpcConfigProps grpcConfigProps() {
        return new GrpcConfigProps();
    }
}
