package me.xra1ny.essentia.inject;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton component container which holds dependency injected object instances.
 *
 * @author xRa1ny
 */
public interface DIContainer {

    @NonNull
    Map<Class<?>, Object> getComponentClassObjectMap();

    default List<Object> getComponentList() {
        return getComponentClassObjectMap().values().stream()
                .toList();
    }

    default <T> List<T> getComponentList(@NonNull Class<T> type) {
        return getComponentList().stream()
                .filter(component -> type.isAssignableFrom(component.getClass()))
                .map(type::cast)
                .toList();
    }

    default boolean isRegistered(@NonNull Class<?> clazz) {
        return getComponentClassObjectMap().containsKey(clazz);
    }

    /**
     * Checks if the given component is registered or not.
     *
     * @param object The object to check for registration.
     * @return True if the supplied object is registered; false otherwise.
     */
    default boolean isRegistered(@NonNull Object object) {
        return getComponentClassObjectMap().containsValue(object);
    }

    void registerComponent(@NonNull Object object);

    void unregisterComponent(@NonNull Class<?> type);

    void unregisterComponent(@NonNull Object object);

    /**
     * Gets the requested component object instance.
     *
     * @param clazz The class.
     * @param <T>   The type.
     * @return The component object instance.
     */
    default <T> Optional<T> getComponent(@NonNull Class<T> clazz) {
        return getComponentClassObjectMap().entrySet().stream()
                .filter(entry -> entry.getKey().equals(clazz))
                .map(Map.Entry::getValue)
                .map(clazz::cast)
                .findFirst();
    }
}
