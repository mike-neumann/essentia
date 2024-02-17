package me.xra1ny.essentia.inject.test.component;

import me.xra1ny.essentia.inject.annotation.Component;

@Component
public class TestComponent2 {
    private final TestComponent3 testComponent3;

    public TestComponent2(TestComponent3 testComponent3) {
        this.testComponent3 = testComponent3;
    }
}
