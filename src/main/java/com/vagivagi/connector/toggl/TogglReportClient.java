package com.vagivagi.connector.toggl;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Component
public class TogglReportClient {
    private final WebClient webClient;

    public TogglReportClient(WebClient.Builder builder, TogglProperties togglProperties) {
        this.webClient = builder
                .baseUrl(togglProperties.getReportBaseUrl())
                .defaultHeader("Authorization", "Basic "
                        + Base64Utils.encodeToString((togglProperties.getApiToken() + ":api_token").getBytes()))
                .build();
    }

    public Mono<TogglDetailedReportResponse> getDetailedReport(Map<String, ?> parameterMap) {
        return this.webClient.get()
                .uri(builder -> builder.path("details").queryParam("user_agent", "hagikazu7@gmail.com")
                        .queryParam("workspace_id","4343760")
                        .queryParam("project_ids","165302293,165302294,165302297,165302299,165302304,165302310,165302313,165302314,165302316")
                        .queryParam("since", LocalDate.now().toString())
                        .queryParam("until",LocalDate.now().toString())
                        .build())
                .retrieve()
                .bodyToMono(TogglDetailedReportResponse.class);
    }
}
