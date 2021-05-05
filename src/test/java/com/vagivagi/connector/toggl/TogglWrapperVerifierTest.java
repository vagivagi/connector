package com.vagivagi.connector.toggl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TogglWrapperVerifierTest {
    private TogglWrapperVerifier togglWrapperVerifier;
    @Mock
    private TogglProperties togglProperties;

    @BeforeEach
    public void setup() {
        togglWrapperVerifier = new TogglWrapperVerifier(togglProperties);
    }

    @Test
    public void invalid_key() {
        when(togglProperties.getMyKey()).thenReturn("validKey");
        TogglWrapperEntryRequestBuilder builder = new TogglWrapperEntryRequestBuilder();
        assertThrows(TogglWrapperAuthenticationException.class,
                () -> togglWrapperVerifier.verify(builder.withKey("invalid").build()), "invalid key");
    }

    @Test
    public void valid_key() {
        when(togglProperties.getMyKey()).thenReturn("validKey");
        TogglWrapperEntryRequestBuilder builder = new TogglWrapperEntryRequestBuilder();
        togglWrapperVerifier.verify(builder.withKey("validKey").build());
    }
}
