package me.xra1ny.essentia.configure.test.config;

import me.xra1ny.essentia.configs.Config;
import me.xra1ny.essentia.configs.annotation.ConfigInfo;
import me.xra1ny.essentia.configs.annotation.Property;

@ConfigInfo("test.properties")
public class PropertiesConfig extends Config<String> {
    @Property("test-string")
    public String testString;
}
