package com.vagivagi.connector;

import com.vagivagi.connector.ifttt.IftttProperties;
import com.vagivagi.connector.toggl.TogglProperties;
import com.vagivagi.connector.toggl.wrapper.TogglWrapperHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
@EnableConfigurationProperties({TogglProperties.class, IftttProperties.class})
public class HomeConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeConnectorApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes(TogglWrapperHandler togglWrapperHandler) {
        return togglWrapperHandler.routes();
    }

    @Bean
    Clock clock() {
        return Clock.system(ZoneId.of("Asia/Tokyo"));
    }

    @Bean
    ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.setMaxPoolSize(40);
        return taskExecutor;
    }
}
