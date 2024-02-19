package me.xra1ny.essentia.configure.test;

import me.xra1ny.essentia.configure.test.config.PropertiesConfig;
import me.xra1ny.essentia.configure.test.config.YMLConfig;
import me.xra1ny.essentia.configure.test.config.YMLObject;

import java.util.List;
import java.util.UUID;

public class EssentiaConfigureTest {
    public static void main(String[] args) {
        final YMLConfig ymlConfig = new YMLConfig();

        ymlConfig.testUuid = UUID.randomUUID();
//        ymlConfig.testObject = new YMLObject();
        ymlConfig.testObjectList = List.of(new YMLObject());
        ymlConfig.save();
        System.out.println(ymlConfig.testUuid);
//        System.out.println(ymlConfig.testObject);
        System.out.println(ymlConfig.testObjectList);

        final PropertiesConfig propertiesConfig = new PropertiesConfig();
    }
}
