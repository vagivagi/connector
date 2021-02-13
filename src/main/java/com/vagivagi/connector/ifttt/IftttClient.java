package com.vagivagi.connector.ifttt;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class IftttClient {
    private final WebClient webClient;
    private final IftttProperties iftttProperties;

    public IftttClient(WebClient.Builder builder, IftttProperties iftttProperties) {
        this.webClient = builder
                .baseUrl(iftttProperties.getBaseUrl())
                .build();
        this.iftttProperties = iftttProperties;
    }

    public Mono<String> triggerEvent(IftttEventEnum iftttEventEnum, Mono<IftttRequestBody> body) {
        return this.webClient.post()
                .uri(iftttEventEnum.getName() + "/with/key/" + iftttProperties.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body, IftttRequestBody.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
