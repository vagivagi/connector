package com.vagivagi.connector.toggl;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;

@Component
public class TogglReportClient {
    private final WebClient webClient;
    private final TogglProperties togglProperties;

    public TogglReportClient(WebClient.Builder builder, TogglProperties togglProperties) {
        this.webClient = builder
                .baseUrl(togglProperties.getReportBaseUrl())
                .defaultHeader("Authorization", "Basic "
                        + Base64Utils.encodeToString((togglProperties.getApiToken() + ":api_token").getBytes()))
                .build();
        this.togglProperties = togglProperties;
    }

    public Mono<TogglDetailedReportResponse> getDetailedReport(LocalDate since, LocalDate until) {
        return this.webClient.get()
                .uri(builder -> builder.path("details").queryParam("user_agent", togglProperties.getUserAgent())
                        .queryParam("workspace_id", togglProperties.getWorkspaceId())
                        .queryParam("project_ids", TogglProjectEnum.getEnglishProjectIds())
                        .queryParam("since", since.toString())
                        .queryParam("until", until.toString())
                        .build())
                .retrieve()
                .bodyToMono(TogglDetailedReportResponse.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .delaySubscription(Duration.ofSeconds(10))
                .log("call client for getting report");
    }
}
