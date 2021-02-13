package com.vagivagi.connector.toggl;

import com.vagivagi.connector.common.ConnectorResponseBody;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

@Service
public class TogglWrapperService {
    private static Logger log = LoggerFactory.getLogger(TogglWrapperService.class);
    private final TogglClient togglClient;
    private final TogglReportClient togglReportClient;

    public TogglWrapperService(TogglClient togglClient, TogglReportClient togglReportClient) {
        this.togglClient = togglClient;
        this.togglReportClient = togglReportClient;
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

    Mono<TogglTimeEntry> convertNumberEntryToWorkProject(Mono<TogglTimeEntry> entryMono) {
        return entryMono.flatMap(
                timeEntry -> {
                    if (NumberUtils.isNumber(timeEntry.getDescription())) {
                        return Mono.just(new TogglTimeEntry(timeEntry.getDescription(), TogglProjectEnum.WORK.getPid()));
                    }
                    return Mono.just(timeEntry);
                }
        );
    }

    Mono<ConnectorResponseBody> restartCurrentTimeEntry() {
        return togglClient.getCurrentEntry()
                .flatMap(
                        timeEntry -> {
                            return this.start(timeEntry.getData().getDescription(), timeEntry.getData().getPid());
                        }
                );
    }

    Mono<String> getStudyReport(LocalDate since, LocalDate until) {
        return togglReportClient.getDetailedReport(since, until)
                .flatMap(
                        report -> {
                            LocalTime t = LocalTime.MIDNIGHT.plusSeconds(report.getTotalGrand() / 1000);
                            String timeFormatted = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(t);
                            return Mono.just(timeFormatted);
                        }
                );
    }

    Mono<TogglWrapperStudyReport> getStudyDetailReport() {
        LocalDate today = LocalDate.now();
        return this.getStudyReport(today, today)
                .flatMap(
                        todayReport -> {
                            LocalDate yesterday = today.minusDays(1);
                            return this.getStudyReport(yesterday, yesterday)
                                    .flatMap(
                                            yesterdayReport -> {
                                                LocalDate start = LocalDate.now().minusMonths(1).plusDays(1);
                                                LocalDate end = LocalDate.now().plusMonths(1).minusDays(1);
                                                return this.getStudyReport(start, end).flatMap(
                                                        monthlyReport -> {
                                                            return Mono.just(new TogglWrapperStudyReport(todayReport, yesterdayReport, monthlyReport));
                                                        }
                                                );
                                            }
                                    );
                        }
                );
    }
}
