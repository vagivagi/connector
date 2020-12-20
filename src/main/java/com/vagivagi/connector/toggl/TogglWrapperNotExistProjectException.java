package com.vagivagi.connector.toggl;

public class TogglWrapperNotExistProjectException extends RuntimeException {
    public TogglWrapperNotExistProjectException(String message) {
        super(message);
    }
}
