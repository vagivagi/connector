package com.vagivagi.connector.ifttt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "ifttt")
public class IftttProperties {
    private final String baseUrl;
    private final String key;

    public IftttProperties(String baseUrl, String key) {
        this.baseUrl = baseUrl;
        this.key = key;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getKey() {
        return key;
    }
}
