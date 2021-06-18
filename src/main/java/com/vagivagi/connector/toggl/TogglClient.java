package com.vagivagi.connector.toggl;

import com.vagivagi.connector.common.LifeUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class TogglClient {
    private final WebClient webClient;

    public TogglClient(WebClient.Builder builder, TogglProperties togglProperties) {
        this.webClient = builder
                .baseUrl(togglProperties.getBaseUrl())
                .defaultHeader("Authorization", "Basic "
                        + Base64Utils.encodeToString((togglProperties.getApiToken() + ":api_token").getBytes()))
                .build();
    }

    public Mono<TogglTimeEntryResponseBody> getCurrentEntry() {
        return this.webClient.get()
                .uri("time_entries/current")
                .retrieve()
                .bodyToMono(TogglTimeEntryResponseBody.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .delaySubscription(Duration.ofSeconds(10))
                .log("call client for getting current entry");
    }

    public Mono<TogglTimeEntryResponseBody> stopEntry(int timeEntryId) {
        return this.webClient.put()
                .uri("time_entries/" + timeEntryId + "/stop")
                .retrieve()
                .bodyToMono(TogglTimeEntryResponseBody.class);
    }

    public Mono<TogglTimeEntryResponseBody> startEntry(Mono<TogglStartEntryRequestBody> body) {
        return this.webClient.post()
                .uri("time_entries/start")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body, TogglStartEntryRequestBody.class)
                .retrieve()
                .bodyToMono(TogglTimeEntryResponseBody.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .delaySubscription(Duration.ofSeconds(10))
                .log("call client for starting entry");
    }

    public Flux<TogglTimeEntry> getTimeEntries() {
        // get entries for 3 days
        return this.webClient.get()
                .uri("time_entries")
                .attribute("start_date", LocalDateTime.now(LifeUtil.ZONE_TOKYO).minusDays(3))
                .attribute("end_date", LocalDateTime.now(LifeUtil.ZONE_TOKYO))
                .retrieve()
                .bodyToFlux(TogglTimeEntry.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .delaySubscription(Duration.ofSeconds(10))
                .log("call client for getting entries");
    }
}
