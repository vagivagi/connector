package com.vagivagi.connector.toggl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TogglWrapperTasks {
    private final TogglWrapperService togglWrapperService;

    public TogglWrapperTasks(TogglWrapperService togglWrapperService) {
        this.togglWrapperService = togglWrapperService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void restartTimeEntry(){
        this.togglWrapperService.restartCurrentTimeEntry();
    }
}
