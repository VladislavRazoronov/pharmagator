package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!test")
@RequiredArgsConstructor
public class SchedulerController {

    private final Scheduler scheduler;

    @GetMapping("/scheduler")
    public void startScheduler() {
        scheduler.schedule();
    }

}
