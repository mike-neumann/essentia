package me.xra1ny.essentia.schedule.test.task;

import lombok.extern.java.Log;
import me.xra1ny.essentia.schedule.annotation.Scheduled;

@Log
public class Task {
    private static int tickCount = 0;

    @Scheduled(1_000)
    public static void scheduled() {
        log.info("tick! " + tickCount);
        tickCount++;
    }
}
