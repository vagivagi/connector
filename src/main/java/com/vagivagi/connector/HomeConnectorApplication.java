package com.vagivagi.connector;

import com.vagivagi.connector.ifttt.IftttProperties;
import com.vagivagi.connector.toggl.TogglProperties;
import com.vagivagi.connector.toggl.TogglWrapperHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
@EnableConfigurationProperties({TogglProperties.class, IftttProperties.class})
@EnableScheduling
public class HomeConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeConnectorApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes(TogglWrapperHandler togglWrapperHandler) {
        return togglWrapperHandler.routes();
    }
}
