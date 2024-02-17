package me.xra1ny.essentia.inject;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomDIContainer<T> implements DIContainer {
    @Getter
    @NonNull
    private final List<T> vitalComponentList = new ArrayList<>();

    @Override
    public @NonNull Map<Class<?>, Object> getComponentClassObjectMap() {
        final Map<Class<?>, Object> map = vitalComponentList.stream()
                .collect(Collectors.toMap(T::getClass, component -> component, (a, b) -> b));
        final Map<Class<?>, Object> deepMap = map.values().stream()
                .filter(object -> object instanceof List<?>)
                .map(object -> (List<?>) object)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Object::getClass, object -> object, (a, b) -> b));

        map.putAll(deepMap);

        return map;
    }

    @Override
    public void registerComponent(@NonNull Object object) {
        if (isRegistered(object)) {
            return;
        }

        vitalComponentList.add((T) object);
    }

    @Override
    public void unregisterComponent(@NonNull Class<?> type) {
        return;
    }

    @Override
    public void unregisterComponent(@NonNull Object object) {
        vitalComponentList.remove((T) object);
    }

    public final <T> List<T> getVitalComponentList(@NonNull Class<T> clazz) {
        return vitalComponentList.stream()
                .filter(vitalComponent -> clazz.isAssignableFrom(vitalComponent.getClass()))
                .map(clazz::cast)
                .toList();
    }
}
