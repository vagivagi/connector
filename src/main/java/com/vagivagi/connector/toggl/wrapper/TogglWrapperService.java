package com.vagivagi.connector.toggl.wrapper;

import com.vagivagi.connector.common.ConnectorResponseBody;
import com.vagivagi.connector.common.LifeUtil;
import com.vagivagi.connector.toggl.*;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
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
                ).take(1).log("get past records").defaultIfEmpty(new TogglTimeEntry(description, pid)).single();
    }

    Mono<TogglTimeEntry> convertNumberEntryToWorkProject(Mono<TogglTimeEntry> entryMono) {
        return entryMono.log("if entry description is number, it is work project.").flatMap(
                timeEntry -> {
                    if (NumberUtils.isNumber(timeEntry.getDescription())) {
                        return Mono.just(new TogglTimeEntry(timeEntry.getDescription(), TogglProjectEnum.WORK.getPid())).log("this is work.");
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
                ).log("restart current entry");
    }

    Mono<String> getStudyReport(LocalDate since, LocalDate until) {
        return togglReportClient.getDetailedReport(since, until)
                .flatMap(
                        report -> {
                            long seconds = report.getTotalGrand() / 1000;
                            String time = String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
                            return Mono.just(time);
                        }
                );
    }

    Mono<TogglWrapperStudyReport> getStudyDetailReport() {
        LocalDate today = LocalDate.now(LifeUtil.ZONE_TOKYO);
        return this.getStudyReport(today, today)
                .flatMap(
                        todayReport -> {
                            LocalDate yesterday = today.minusDays(1);
                            return this.getStudyReport(yesterday, yesterday)
                                    .flatMap(
                                            yesterdayReport -> {
                                                LocalDate start = today.withDayOfMonth(1);
                                                LocalDate end = today.withDayOfMonth(today.lengthOfMonth());
                                                return this.getStudyReport(start, end)
                                                        .log(start.toString())
                                                        .log(end.toString())
                                                        .flatMap(
                                                                monthlyReport -> {
                                                                    int margin = monthlyReport.length() - 7;
                                                                    return Mono.just(new TogglWrapperStudyReport(this.addMarginReport(todayReport, margin),
                                                                            this.addMarginReport(yesterdayReport, margin),
                                                                            monthlyReport));
                                                                }
                                                        );
                                            }
                                    );
                        }
                );
    }

    private String addMarginReport(String report, int margin) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < margin; i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append(report);
        return stringBuilder.toString();
    }
}
