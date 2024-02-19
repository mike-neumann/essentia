package me.xra1ny.essentia.configure.test.config;

import lombok.ToString;
import me.xra1ny.essentia.configs.annotation.ConfigObject;
import me.xra1ny.essentia.configs.annotation.Property;

@ToString
@ConfigObject
public class YMLObject {
    @Property
    public String testString;

    public int testIgnoredInt;

    @Property
    public YMLObject2 ymlObject2 = new YMLObject2();
}