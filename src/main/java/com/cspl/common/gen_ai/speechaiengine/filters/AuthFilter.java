package com.cspl.common.gen_ai.speechaiengine.filters;
import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthFilter implements WebFilter {

    /**
     * The type Application utils.
     */
    private final ApplicationUtils applicationUtils;

    /**
     * The authentication properties
     */
    private final AppProperties.AuthProperties authProperties;

    /**
     * The type Auth filter.
     *
     * @param exchange
     * @param chain
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, String> headersMap = new HashMap<>();

        exchange.getRequest().getHeaders().forEach((headerName, values) -> {
            if (!values.isEmpty()) {
                headersMap.put(headerName, values.get(0));
            }
        });

        String path = exchange.getRequest().getURI().getPath();
        if (authProperties.getPublicPaths().stream().anyMatch(path::contains)) {
            return chain.filter(exchange);
        }

        try {
//            applicationUtils.validateApiHeader(headersMap, APIConstants.REQUEST_KEYS.AUTH_KEY);
        } catch (RuntimeException ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}

