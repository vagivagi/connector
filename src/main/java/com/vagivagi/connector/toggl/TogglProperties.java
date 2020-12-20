package com.vagivagi.connector.toggl;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "toggl")
public class TogglProperties {

    private final String baseUrl;
    private final String apiToken;
    private final String myKey;

    public TogglProperties(String baseUrl, String apiToken, String myKey) {
        this.baseUrl = baseUrl;
        this.apiToken = apiToken;
        this.myKey = myKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getMyKey() {
        return myKey;
    }
}
