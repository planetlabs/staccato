package com.planet.staccato.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author joshfix
 * Created on 2/28/19
 */
@Slf4j
//@Component
public class ParameterValidator implements WebFilter {

    private List<String> apiParameters;

    @PostConstruct
    public void init() {
        for (Method method :  ApiApi.class.getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase("getitems")) {
                Parameter[] parameters = method.getParameters();
                apiParameters = new ArrayList<>(parameters.length);
                for (Parameter parameter : parameters) {
                    apiParameters.add(parameter.getName().toLowerCase());
                }
            }
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        MultiValueMap<String, String> requestParams = exchange.getRequest().getQueryParams();
        requestParams.keySet().forEach(key -> {
            if (!apiParameters.contains(key.toLowerCase())) {
                throw new InvalidParameterException("Invalid request parameter: " + key);
            }
        });

        return chain.filter(exchange);

    }
}
