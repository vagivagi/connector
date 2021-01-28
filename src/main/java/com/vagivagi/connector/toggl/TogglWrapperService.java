package com.vagivagi.connector.toggl;

import com.vagivagi.connector.common.ConnectorResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Optional;

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

    Mono<ConnectorResponseBody> start(String description, int pid) {
        return togglClient.startEntry(
                Mono.just(TogglStartEntryRequestBody.builder()
                        .description(description.trim())
                        .pid(pid)
                        .createdWith("vagivagi api")
                        .build())).doOnError(response -> log.error("error caused", response))
                .log("start entry")
                .map(body -> ConnectorResponseBody.builder().message("OK").body(body).build());
    }

    Mono<ConnectorResponseBody> specificStart(String description, TogglProjectEnum togglProjectEnum) {
        return togglClient.startEntry(
                Mono.just(TogglStartEntryRequestBody.builder()
                        .description(description.trim())
                        .pid(togglProjectEnum.getPid())
                        .createdWith("vagivagi api")
                        .build())).doOnError(response -> log.error("error caused", response))
                .log("start entry")
                .map(body -> ConnectorResponseBody.builder().message("OK").body(body).build());
    }

    Mono<TogglTimeEntry> getTimeEntryFromPastRecord(String description, int pid) {
        return togglClient.getTimeEntries()
                .filter(
                        timeEntry ->
                                Optional.ofNullable(description).orElse("").trim()
                                        .equalsIgnoreCase(
                                                Optional.ofNullable(timeEntry.getDescription())
                                                        .orElse("").trim())
                ).sort(Comparator.comparing(TogglTimeEntry::getAt).reversed())
                .take(1).defaultIfEmpty(new TogglTimeEntry(description, pid)).single();
    }

    Mono<ConnectorResponseBody> restartCurrentTimeEntry() {
        return togglClient.getCurrentEntry()
                .flatMap(
                        timeEntry -> {
                            return this.start(timeEntry.getData().getDescription(), timeEntry.getData().getPid());
                        }
                );
    }
}
