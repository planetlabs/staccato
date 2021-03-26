package com.planet.staccato.api;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.exceptions.InvalidParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verifies that URL parameters passed in GET requests are valid.  If not valid, an InvalidParameterException is thrown
 *
 * @author joshfix
 * Created on 2/28/19
 */
@Slf4j
@Component
public class ParameterValidator implements WebFilter {

    private List<String> apiParameters = new ArrayList<>();
    private static final String[] PARAMETERS_JAVA_HATES = {"filter-lang", "filter-crs"};

    @PostConstruct
    public void init() {
        for (Field field : SearchRequest.class.getDeclaredFields()) {
            apiParameters.add(field.getName().toLowerCase());
        }

        apiParameters.addAll(Arrays.asList(PARAMETERS_JAVA_HATES));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        MultiValueMap<String, String> requestParams = exchange.getRequest().getQueryParams();
        requestParams.keySet().forEach(key -> {
            if (!apiParameters.contains(key.toLowerCase())) {
                throw new InvalidParameterException(key, apiParameters);
            }
        });

        return chain.filter(exchange);
    }
}
