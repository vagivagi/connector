package com.vagivagi.connector.toggl;

import com.vagivagi.connector.common.ConnectorResponseBody;
import com.vagivagi.connector.ifttt.IftttService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class TogglWrapperHandler {
    private final TogglWrapperService togglWrapperService;
    private final TogglWrapperVerifier togglWrapperVerifier;
    private final IftttService iftttService;

    public TogglWrapperHandler(TogglWrapperService togglWrapperService
            , TogglWrapperVerifier togglWrapperVerifier, IftttService iftttService) {
        this.togglWrapperService = togglWrapperService;
        this.togglWrapperVerifier = togglWrapperVerifier;
        this.iftttService = iftttService;
    }

    public RouterFunction<ServerResponse> routes() {
        return route(POST("/toggl/start"), this::startSelectable)
                .andRoute(POST("/toggl/startProject"), this::startProject)
                .andRoute(POST("/toggl/goHome"), this::goHome)
                .andRoute(POST("/toggl/goingOut"), this::goingOut);
    }

    Mono<ServerResponse> startSelectable(ServerRequest request) {
        return request.bodyToMono(TogglWrapperEntryRequest.class) //
                .flatMap(payload -> {
                    togglWrapperVerifier.verify(payload);
                    return togglWrapperService.getTimeEntryFromPastRecord(payload.getDescription())
                            .defaultIfEmpty(new TogglTimeEntry(payload.getDescription(), payload.getPid()))
                            .log("get pid")
                            .flatMap(
                                    timeEntry -> {
                                        return ServerResponse.ok()
                                                .body(this.togglWrapperService.start(payload.getDescription(), timeEntry.getPid()),
                                                        ConnectorResponseBody.class);
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
                    if (isSleeping()) {
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
                    if (isSleeping()) {
                        // now sleep
                        return ServerResponse.ok()
                                .body(Mono.just(ConnectorResponseBody.builder().message("Now sleeping").build())
                                        , ConnectorResponseBody.class);
                    }
                    iftttService.triggerLightChange()
                            .doOnError(response -> errorResponse(response))
                            .log("light change").subscribe();
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

    Mono<ServerResponse> errorResponse(Throwable e) {
        return Mono.error(() -> e);
    }

    private boolean isSleeping() {
        int nowHour = LocalDateTime.now(ZoneId.of("Asia/Tokyo")).getHour();
        return 0 <= nowHour && nowHour <= 7;
    }
}
