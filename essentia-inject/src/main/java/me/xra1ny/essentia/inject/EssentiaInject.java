package me.xra1ny.essentia.inject;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.xra1ny.essentia.inject.annotation.Component;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

/**
 * EssentiaInject's main class, used to initialise EssentiaInject
 *
 * @author xRa1ny
 */
public class EssentiaInject {
    @Getter
    @NonNull
    private static final List<String> packageNameList = new ArrayList<>();

    @Getter
    private static DIContainer diContainer;

    public static void run(@NonNull DIContainer diContainer, @NonNull String @NonNull ... packageNames) {
        packageNameList.addAll(List.of(packageNames));
        EssentiaInject.diContainer = diContainer;

        run();
    }

    public static void run(@NonNull String @NonNull ... packageNames) {
        run(new EssentiaDIContainer(), packageNames);
    }

    private static void run() {
        final List<Class<?>> componentClassList = getComponentClassList();

        // attempt to get instance
        // this will also initialise any missing components
        componentClassList.forEach(componentClass -> {
            try {
                DIFactory.getInstance(componentClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("error while registering component %s"
                        .formatted(componentClass.getSimpleName()));
            }
        });
    }

    public static void run(@NonNull String packageName, @NonNull DIContainer diContainer) {
        packageNameList.add(packageName);
        EssentiaInject.diContainer = diContainer;

        run();
    }

    @SneakyThrows
    public static void run(@NonNull String packageName, @NonNull Class<? extends DIContainer> diContainerClass) {
        run(packageName, diContainerClass.getConstructor().newInstance());
    }

    public static List<Class<?>> getComponentClassList() {
        return new Reflections(packageNameList).getTypesAnnotatedWith(Component.class).stream()
                .toList();
    }
}