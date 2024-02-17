package me.xra1ny.essentia.inject;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class EssentiaDIContainer implements DIContainer {
    /**
     * Holds all currently registered di components.
     */
    @Getter
    @NonNull
    private final Map<Class<?>, Object> componentClassObjectMap = new HashMap<>();

    @Override
    public void registerComponent(@NonNull Object object) {
        if(isRegistered(object)) {
            return;
        }

        componentClassObjectMap.put(object.getClass(), object);
    }

    @Override
    public void unregisterComponent(@NonNull Class<?> type) {
        componentClassObjectMap.remove(type);
    }

    @Override
    public void unregisterComponent(@NonNull Object object) {
        componentClassObjectMap.remove(object.getClass(), object);
    }
}
