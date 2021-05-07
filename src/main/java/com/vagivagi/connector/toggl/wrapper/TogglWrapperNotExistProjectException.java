package com.vagivagi.connector.toggl.wrapper;

public class TogglWrapperNotExistProjectException extends RuntimeException {
    public TogglWrapperNotExistProjectException(String message) {
        super(message);
    }
}
