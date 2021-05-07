package com.vagivagi.connector.toggl.wrapper;

import com.vagivagi.connector.common.ConnectorResponseBody;
import com.vagivagi.connector.common.LifeUtil;
import com.vagivagi.connector.ifttt.IftttService;
import com.vagivagi.connector.toggl.TogglProjectEnum;
import com.vagivagi.connector.toggl.TogglTimeEntry;
import com.vagivagi.connector.toggl.TogglTimeEntryResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TogglWrapperHandlerTest {
    WebTestClient client;

    @Mock
    private TogglWrapperService togglWrapperService;
    @Mock
    private TogglWrapperVerifier togglWrapperVerifier;
    @Mock
    private IftttService iftttService;
    @Mock
    private LifeUtil lifeUtil;

    @BeforeEach
    public void setUp() {
        TogglWrapperHandler togglWrapperHandler =
                new TogglWrapperHandler(togglWrapperService, togglWrapperVerifier, iftttService, lifeUtil);
        RouterFunction routerFunction = togglWrapperHandler.routes();
        client = WebTestClient.bindToRouterFunction(routerFunction).configureClient().build();
    }

    @Test
    public void startSelectable() {
        when(togglWrapperService.start("description", 0))
                .thenReturn(Mono.just(ConnectorResponseBody.builder()
                        .message("success").body(new TogglTimeEntryResponseBody()).build()));
        when(togglWrapperService.getTimeEntryFromPastRecord("description", 0))
                .thenReturn(Mono.just(new TogglTimeEntry()));
        when(togglWrapperService.convertNumberEntryToWorkProject(any()))
                .thenReturn(Mono.just(new TogglTimeEntry("description", 0)));
        client
                .post()
                .uri("/toggl/start")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new TogglWrapperEntryRequest("description", 0, 0, "test-key")), TogglWrapperEntryRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void startProject() {
        when(togglWrapperService.specificStart("description", TogglProjectEnum.SLEEP))
                .thenReturn(Mono.just(new ConnectorResponseBody("success", new TogglTimeEntryResponseBody())));
        client
                .post()
                .uri(builder -> builder.path("/toggl/startProject").queryParam("projectName", "Sleep").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new TogglWrapperEntryRequest("description", 0, 0, "test-key")), TogglWrapperEntryRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void goHome() {
        when(togglWrapperService.specificStart("description", TogglProjectEnum.HOUSEWORK))
                .thenReturn(Mono.just(new ConnectorResponseBody("success", new TogglTimeEntryResponseBody())));
        when(iftttService.triggerLightChange()).thenReturn(Mono.just("success"));
        when(lifeUtil.isSleeping()).thenReturn(false);
        client
                .post()
                .uri(builder -> builder.path("/toggl/goHome").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new TogglWrapperEntryRequest("description", 0, 0, "test-key")), TogglWrapperEntryRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void goingOut() {
        when(togglWrapperService.specificStart("description", TogglProjectEnum.GOING_OUT))
                .thenReturn(Mono.just(new ConnectorResponseBody("success", new TogglTimeEntryResponseBody())));
        when(lifeUtil.isSleeping()).thenReturn(false);
        client
                .post()
                .uri(builder -> builder.path("/toggl/goingOut").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new TogglWrapperEntryRequest("description", 0, 0, "test-key")), TogglWrapperEntryRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
