package com.vagivagi.connector.toggl.wrapper;

import com.vagivagi.connector.common.ConnectorResponseBody;
import com.vagivagi.connector.common.LifeUtil;
import com.vagivagi.connector.ifttt.IftttService;
import com.vagivagi.connector.toggl.TogglProjectEnum;
import com.vagivagi.connector.toggl.TogglTimeEntry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class TogglWrapperHandler {
    private final TogglWrapperService togglWrapperService;
    private final TogglWrapperVerifier togglWrapperVerifier;
    private final IftttService iftttService;
    private final LifeUtil lifeUtil;

    public TogglWrapperHandler(TogglWrapperService togglWrapperService
            , TogglWrapperVerifier togglWrapperVerifier, IftttService iftttService, LifeUtil lifeUtil) {
        this.togglWrapperService = togglWrapperService;
        this.togglWrapperVerifier = togglWrapperVerifier;
        this.iftttService = iftttService;
        this.lifeUtil = lifeUtil;
    }

    public RouterFunction<ServerResponse> routes() {
        return route(POST("/toggl/start"), this::startSelectable)
                .andRoute(POST("/toggl/startProject"), this::startProject)
                .andRoute(POST("/toggl/goHome"), this::goHome)
                .andRoute(POST("/toggl/goingOut"), this::goingOut)
                .andRoute(POST("/toggl/restart"), this::restartCurrentEntry)
                .andRoute(POST("/toggl/studyReport"), this::getStudyReport)
                .andRoute(POST("/toggl/sendReport"), this::sendStudyReportIfttt);
    }

    Mono<ServerResponse> startSelectable(ServerRequest request) {
        return request.bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    return togglWrapperService.getTimeEntryFromPastRecord(payload.getDescription(), payload.getPid())
                            .defaultIfEmpty(new TogglTimeEntry(payload.getDescription(), payload.getPid()))
                            .log("get pid")
                            .flatMap(
                                    timeEntry -> {
                                        return this.togglWrapperService.convertNumberEntryToWorkProject(Mono.just(timeEntry)).flatMap(
                                                entry -> {
                                                    return ServerResponse.ok()
                                                            .body(this.togglWrapperService.start(entry.getDescription(), entry.getPid()),
                                                                    ConnectorResponseBody.class);
                                                }
                                        );

                                    }
                            );
                });
    }

    Mono<ServerResponse> startProject(ServerRequest request) {
        return request
                .bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    return ServerResponse.ok()
                            .body(this.togglWrapperService.specificStart(payload.getDescription(),
                                    TogglProjectEnum.toToggleProjectEnum(request.queryParam("projectName").get()))
                                    , ConnectorResponseBody.class);
                }).onErrorResume(e ->
                        ServerResponse
                                .badRequest()
                                .body(Mono.just(ConnectorResponseBody
                                        .builder()
                                        .message("Error " + e.getMessage())
                                        .build()), ConnectorResponseBody.class));
    }

    Mono<ServerResponse> goHome(ServerRequest request) {
        return request
                .bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    if (lifeUtil.isSleeping()) {
                        // now sleep
                        return ServerResponse.ok()
                                .body(Mono.just(ConnectorResponseBody.builder().message("Now sleeping").build())
                                        , ConnectorResponseBody.class);
                    }
                    iftttService.triggerLightChange()
                            .doOnError(response -> errorResponse(response))
                            .log("light change").subscribe();
                    return ServerResponse.ok()
                            .body(this.togglWrapperService.specificStart(payload.getDescription(), TogglProjectEnum.HOUSEWORK)
                                    , ConnectorResponseBody.class)
                            .onErrorResume(e ->
                                    ServerResponse
                                            .badRequest()
                                            .body(Mono.just(ConnectorResponseBody
                                                    .builder()
                                                    .message("Error " + e.getMessage())
                                                    .build()), ConnectorResponseBody.class));
                });
    }

    Mono<ServerResponse> goingOut(ServerRequest request) {
        return request
                .bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    if (lifeUtil.isSleeping()) {
                        // now sleep
                        return ServerResponse.ok()
                                .body(Mono.just(ConnectorResponseBody.builder().message("Now sleeping").build())
                                        , ConnectorResponseBody.class);
                    }
                    return ServerResponse.ok()
                            .body(this.togglWrapperService.specificStart(payload.getDescription(), TogglProjectEnum.GOING_OUT)
                                    , ConnectorResponseBody.class)
                            .onErrorResume(e ->
                                    ServerResponse
                                            .badRequest()
                                            .body(Mono.just(ConnectorResponseBody
                                                    .builder()
                                                    .message("Error " + e.getMessage())
                                                    .build()), ConnectorResponseBody.class));
                });
    }

    Mono<ServerResponse> restartCurrentEntry(ServerRequest request) {
        return request
                .bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    return ServerResponse.ok()
                            .body(this.togglWrapperService.restartCurrentTimeEntry()
                                    , ConnectorResponseBody.class)
                            .onErrorResume(e ->
                                    ServerResponse
                                            .badRequest()
                                            .body(Mono.just(ConnectorResponseBody
                                                    .builder()
                                                    .message("Error " + e.getMessage())
                                                    .build()), ConnectorResponseBody.class));
                });
    }

    Mono<ServerResponse> getStudyReport(ServerRequest request) {
        return request
                .bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    return ServerResponse.ok()
                            .body(this.togglWrapperService.getStudyReport(LocalDate.now(), LocalDate.now()), String.class)
                            .onErrorResume(e ->
                                    ServerResponse
                                            .badRequest()
                                            .body(Mono.just(ConnectorResponseBody
                                                    .builder()
                                                    .message("Error " + e.getMessage())
                                                    .build()), ConnectorResponseBody.class));
                });
    }

    Mono<ServerResponse> sendStudyReportIfttt(ServerRequest request) {
        return request
                .bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    return this.togglWrapperService.getStudyDetailReport()
                            .flatMap(
                                    report -> {
                                        return ServerResponse.ok()
                                                .body(iftttService.triggerReport(report.getToday(), report.getYesterday(), report.getMonthly()), String.class)
                                                .onErrorResume(e ->
                                                        ServerResponse
                                                                .badRequest()
                                                                .body(Mono.just(ConnectorResponseBody
                                                                        .builder()
                                                                        .message("Error " + e.getMessage())
                                                                        .build()), ConnectorResponseBody.class));
                                    }
                            );
                });
    }

    Mono<ServerResponse> errorResponse(Throwable e) {
        return Mono.error(() -> e);
    }

}
