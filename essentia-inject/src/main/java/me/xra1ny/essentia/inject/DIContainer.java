package me.xra1ny.essentia.inject;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Singleton component container which holds dependency injected object instances.
 *
 * @author xRa1ny
 */
public interface DIContainer {
    @NonNull
    Map<Class<?>, Object> getComponentClassObjectMap();

    @NonNull
    default List<Object> getComponents() {
        return getComponentClassObjectMap().values().stream()
                .toList();
    }

    @Nullable
    default <T> List<T> getComponentsByType(@NonNull Class<T> type) {
        return getComponents().stream()
                .filter(component -> type.isAssignableFrom(component.getClass()))
                .map(type::cast)
                .toList();
    }

    default boolean isRegisteredByType(@NonNull Class<?> type) {
        return getComponentClassObjectMap().containsKey(type);
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

    void unregisterComponentByType(@NonNull Class<?> type);

    void unregisterComponent(@NonNull Object object);

    /**
     * Gets the requested component object instance.
     *
     * @param type The class.
     * @param <T>  The type.
     * @return The component object instance; or null.
     */
    @Nullable
    default <T> T getComponentByType(@NonNull Class<T> type) {
        return getComponentClassObjectMap().entrySet().stream()
                .filter(entry -> entry.getKey().equals(type))
                .map(Map.Entry::getValue)
                .map(type::cast)
                .findFirst()
                .orElse(null);
    }
}