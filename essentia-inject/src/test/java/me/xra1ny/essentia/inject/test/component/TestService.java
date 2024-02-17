package me.xra1ny.essentia.inject.test.component;

import me.xra1ny.essentia.inject.annotation.Component;

@Component
public class TestService {
    private final TestComponent testComponent;
    private final TestComponent2 testComponent2;

    public TestService(TestComponent testComponent, TestComponent2 testComponent2) {
        this.testComponent = testComponent;
        this.testComponent2 = testComponent2;
    }
}
