package com.vagivagi.connector.toggl;

import com.vagivagi.connector.common.ConnectorResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TogglWrapperService {
    private static Logger log = LoggerFactory.getLogger(TogglWrapperService.class);
    private final TogglClient togglClient;

    public TogglWrapperService(TogglClient togglClient) {
        this.togglClient = togglClient;
    }

    Mono<ConnectorResponseBody> stop(TogglWrapperEntryRequest request) {
        return togglClient.getCurrentEntry().flatMap(
                entry -> togglClient.stopEntry(entry.getData().getId()))
                .log("stop entry").map(body -> ConnectorResponseBody.builder().message("OK").body(body).build());
    }

    Mono<ConnectorResponseBody> start(TogglWrapperEntryRequest request) {
        return togglClient.startEntry(
                Mono.just(TogglStartEntryRequestBody.builder()
                        .description(request.getDescription())
                        .pid(request.getPid())
                        .createdWith("vagivagi api")
                        .build())).doOnError(response -> log.error("error caused", response))
                .log("start entry")
                .map(body -> ConnectorResponseBody.builder().message("OK").body(body).build());
    }

    Mono<ConnectorResponseBody> specificStart(String description, TogglProjectEnum togglProjectEnum) {
        return togglClient.startEntry(
                Mono.just(TogglStartEntryRequestBody.builder()
                        .description(description)
                        .pid(togglProjectEnum.getPid())
                        .createdWith("vagivagi api")
                        .build())).doOnError(response -> log.error("error caused", response))
                .log("start entry")
                .map(body -> ConnectorResponseBody.builder().message("OK").body(body).build());
    }
}
