package com.boundlessgeo.staccato.rsocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for RSocket.
 *
 * @author joshfix
 * Created on 10/4/18
 */
@Data
@ConfigurationProperties("staccato.rsocket")
public class RSocketConfigProps {

    private int port = 7000;
}
