package com.example.demo.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTest {

    @Scheduled(fixedRate = 5000)
    public void testRate() {
        System.out.println("startRate: " + new Date());
    }

    @Scheduled(fixedDelay = 5000)
    public void testDelay() {
        System.out.println("startDelay: " + new Date());
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void testCron() {
        System.out.println("startCron: " + new Date());
    }
}
