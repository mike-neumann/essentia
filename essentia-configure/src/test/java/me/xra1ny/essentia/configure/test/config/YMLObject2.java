package me.xra1ny.essentia.configure.test.config;

import me.xra1ny.essentia.configs.annotation.Property;

public class YMLObject2 {
    @Property(boolean.class)
    public boolean testBoolean;

    private boolean ignoredBoolean;
}
