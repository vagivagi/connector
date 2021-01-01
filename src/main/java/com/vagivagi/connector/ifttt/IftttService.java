package com.vagivagi.connector.ifttt;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class IftttService {
    private final IftttClient iftttClient;

    public IftttService(IftttClient iftttClient) {
        this.iftttClient = iftttClient;
    }

    public Mono<String> triggerLightChange(){
        return iftttClient.triggerEvent(IftttEventEnum.LIGHT_0N);
    }
}
