package com.vagivagi.connector.toggl;

import org.springframework.stereotype.Component;

@Component
public class TogglWrapperVerifier {
    private final TogglProperties togglProperties;

    public TogglWrapperVerifier(TogglProperties togglProperties) {
        this.togglProperties = togglProperties;
    }

    public void verify(TogglWrapperEntryRequest payload) {
        if (!togglProperties.getMyKey().equals(payload.getKey())) {
            throw new TogglWrapperAuthenticationException("invalid key");
        }
    }
}
