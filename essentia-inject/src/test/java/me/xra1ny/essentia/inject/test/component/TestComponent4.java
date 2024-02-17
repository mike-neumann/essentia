package me.xra1ny.essentia.inject.test.component;

import me.xra1ny.essentia.inject.annotation.Component;

@Component
public class TestComponent4 {
    private final TestComponent3 testComponent3;

    public TestComponent4(TestComponent3 testComponent3) {
        this.testComponent3 = testComponent3;
    }
}
