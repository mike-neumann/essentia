package me.xra1ny.essentia.configure.test;

import me.xra1ny.essentia.configure.test.config.PropertiesConfig;
import me.xra1ny.essentia.configure.test.config.YMLConfig;
import me.xra1ny.essentia.configure.test.config.YMLObject;

import java.util.UUID;

public class EssentiaConfigureTest {
    public static void main(String[] args) {
        final YMLConfig ymlConfig = new YMLConfig();

        ymlConfig.testUuid = UUID.randomUUID();
        ymlConfig.testObject = new YMLObject();
        ymlConfig.save();

        final PropertiesConfig propertiesConfig = new PropertiesConfig();
    }
}
