package me.xra1ny.essentia.inject.test;

import lombok.extern.java.Log;
import me.xra1ny.essentia.inject.EssentiaInject;

@Log
public class EssentiaInjectTest {
    public static void main(String[] args) {
        EssentiaInject.run(EssentiaInjectTest.class.getPackageName());

        log.info("injectable: " + EssentiaInject.getComponentClassList());
        log.info("injected: " + EssentiaInject.getDiContainer().getComponentClassObjectMap());
    }
}
