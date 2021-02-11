package com.vagivagi.connector.toggl;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "toggl")
public class TogglProperties {

    private final String baseUrl;
    private final String reportBaseUrl;
    private final String apiToken;
    private final String myKey;

    public TogglProperties(String baseUrl, String reportBaseUrl, String apiToken, String myKey) {
        this.baseUrl = baseUrl;
        this.reportBaseUrl = reportBaseUrl;
        this.apiToken = apiToken;
        this.myKey = myKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getReportBaseUrl() {
        return reportBaseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getMyKey() {
        return myKey;
    }
}
