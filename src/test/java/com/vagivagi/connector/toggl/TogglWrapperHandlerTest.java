package com.vagivagi.connector.toggl;

import com.vagivagi.connector.ifttt.IftttService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TogglWrapperHandlerTest {
    WebTestClient client;

    @Autowired
    RouterFunction<ServerResponse> routerFunction;
    @Autowired
    private TogglWrapperService togglWrapperService;
    @Autowired
    private TogglWrapperVerifier togglWrapperVerifier;
    @Autowired
    private IftttService iftttService;

    @BeforeEach
    public void setUp() {
        client = WebTestClient.bindToRouterFunction(routerFunction).configureClient().build();
    }

    @Test
    public void startSelectable() {
        client
                .post()
                .uri("/toggl/start")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new TogglWrapperEntryRequest("", 0, 0, "test-key")), TogglWrapperEntryRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .valueEquals("Location", "");
    }
}
