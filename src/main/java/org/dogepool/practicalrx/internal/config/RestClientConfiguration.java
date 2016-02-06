package org.dogepool.practicalrx.internal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration {

    @Value(value = "${rest.client.readTimeoutMs:2000}")
    private int readTimeout;

    @Value(value = "${rest.client.connectTimeoutMs:2000}")
    private int connectTimeout;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(createFactory());
    }

    private ClientHttpRequestFactory createFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        return factory;
    }
}
