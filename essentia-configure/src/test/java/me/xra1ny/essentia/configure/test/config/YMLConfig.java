package me.xra1ny.essentia.configure.test.config;

import me.xra1ny.essentia.configs.Config;
import me.xra1ny.essentia.configs.annotation.ConfigInfo;
import me.xra1ny.essentia.configs.annotation.Property;
import me.xra1ny.essentia.configs.processor.YMLFileProcessor;

import java.util.UUID;

@ConfigInfo(value = "test.yml", processor = YMLFileProcessor.class)
public class YMLConfig extends Config<Object> {
    @Property("test-uuid")
    public UUID testUuid;

    @Property("test-object")
    public YMLObject testObject;
}