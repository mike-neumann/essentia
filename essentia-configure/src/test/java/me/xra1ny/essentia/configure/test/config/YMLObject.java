package me.xra1ny.essentia.configure.test.config;

import lombok.ToString;
import me.xra1ny.essentia.configs.annotation.Property;

@ToString
public class YMLObject {
    @Property(String.class)
    public String testString;

    public int testIgnoredInt;

    @Property(YMLObject2.class)
    public YMLObject2 ymlObject2 = new YMLObject2();
}