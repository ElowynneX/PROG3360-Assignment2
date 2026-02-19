package org.example.orderservice.Config;

import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UnleashConfiguration {

    @Bean
    public Unleash unleash() {
        String apiUrl = System.getenv("UNLEASH_API_URL");
        String token = System.getenv("UNLEASH_API_TOKEN");
        String project = System.getenv("UNLEASH_PROJECT");

        if (apiUrl == null) apiUrl = "http://unleash-server:4242/api";
        if (token == null) token = "";
        if (project == null) project = "default";

        UnleashConfig config = UnleashConfig.builder()
                .appName("order-service")
                .instanceId("order-service-instance")
                .unleashAPI(apiUrl)
                .apiKey(token)
                .projectName(project)
                .build();

        return new DefaultUnleash(config);
    }
}
