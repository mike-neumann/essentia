package me.xra1ny.essentia.inject;

import lombok.NonNull;
import lombok.extern.java.Log;
import me.xra1ny.essentia.inject.annotation.AfterInit;
import me.xra1ny.essentia.inject.annotation.Component;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Singleton factory class responsible for managing di components.
 *
 * @author xRa1ny
 */
@Log
public class DIFactory {
    /**
     * Tracks all currently creating component instances to validate for any circular dependencies.
     */
    private static final List<Class<?>> recursiveChain = new ArrayList<>();

    /**
     * Attempts to get a dependency injected instance of the given class.
     *
     * @param type The class to dependency inject.
     * @param <T>   The type.
     * @return The dependency injected instance.
     */
    public static <T> T getInstance(@NonNull Class<T> type) throws IllegalAccessException {
        recursiveChain.add(type);

        final Optional<Component> optionalComponent = Optional.ofNullable(type.getAnnotation(Component.class));

        optionalComponent.ifPresent(component -> {
            // initialize dependencies
            for(Class<?> dependency : component.dependsOn()) {
                try {
                    getInstance(dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // attempt to fetch already registered instance...
        if (EssentiaInject.getDiContainer().isRegistered(type)) {
            recursiveChain.remove(type);

            return EssentiaInject.getDiContainer().getComponent(type)
                    .orElseThrow();
        }

        try {
            final T component = createInstance(type);

            callAfterInitMethods(component);
            EssentiaInject.getDiContainer().registerComponent(component);

            optionalComponent.ifPresent(componentAnnotation -> {
                // initialize after...
                for(Class<?> after : componentAnnotation.after()) {
                    try {
                        getInstance(after);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            recursiveChain.remove(type);

            return component;
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extracts a dependency injectable constructor from the given class.
     *
     * @param type The class.
     * @param <T>   The type.
     * @return The dependency injectable constructor.
     * @throws NoSuchMethodException If no dependency injectable constructor was found.
     */
    private static <T> Constructor<? extends T> getConstructor(@NonNull Class<? extends T> type) throws NoSuchMethodException {
        // attempt to get default constructor...
        Constructor<? extends T> viableConstructor = null;

        try {
            viableConstructor = type.getDeclaredConstructor();

            // default viableConstructor found, return...
            return viableConstructor;
        } catch (NoSuchMethodException e) {
            // default viableConstructor not present, decipher viable constructor..

            for (Constructor<?> constructor : type.getConstructors()) {
                // iterate over every parameter to catch optimal constructor viable for dependency injection...
                final List<Class<?>> parameterList = new ArrayList<>();

                for (Parameter parameter : constructor.getParameters()) {
                    final Class<?> parameterType = parameter.getType();

                    // by default every class is viable for dependency injection, no need to check if they are viable

                    parameterList.add(parameterType);
                }

                // when the list size matches our constructor parameter count, we know we have the right constructor which is viable for di.
                if (parameterList.size() == constructor.getParameterCount()) {
                    //noinspection unchecked
                    viableConstructor = (Constructor<T>) constructor;

                    break;
                }
            }

            if (viableConstructor == null) {
                throw new NoSuchMethodException("no dependency injectable constructor found for class " + type.getSimpleName());
            }

            return viableConstructor;
        }
    }

    /**
     * Creates a dependency injected instance of the given class.
     *
     * @param type The class.
     * @param <T>   The type.
     * @return The dependency injected instance.
     * @throws NoSuchMethodException     If no dependency injectable constructor was found.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static <T> T createInstance(@NonNull Class<T> type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // if the type we are trying to create, is an interface, scan for any implementation of it...
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            final Optional<Class<? extends T>> optionalImplementation = getImplementation(type);

            if(optionalImplementation.isEmpty()) {
                log.severe("No implementation found for " + type.getSimpleName());
            }

            return getInstance((Class<T>) optionalImplementation.get());
        }

        // component if not already registered.
        final Constructor<? extends T> constructor = getConstructor(type);
        final List<Object> constructorParameterList = new ArrayList<>();

        // we know that every parameter is a component.
        for (Parameter parameter : constructor.getParameters()) {
            // check if parameter component is already registered,
            // if so, return registered component.

            final Class<?> parameterType = parameter.getType();

            // check if component is already attempted to be created.
            if (recursiveChain.contains(parameterType)) {
                recursiveChain.clear();

                // if so, we are trying to inject circular dependencies, which are not possible.
                throw new IllegalAccessException("circular dependency detected!  " + type.getSimpleName() + " <~> " + parameterType.getSimpleName());
            }

            if (EssentiaInject.getDiContainer().isRegistered(parameterType)) {
                constructorParameterList.add(EssentiaInject.getDiContainer().getComponent(parameterType)
                        .orElseThrow());
            } else {
                // else create new component instance and register.
                final Object newComponent = getInstance((Class<T>) parameterType);

                // also call any @AfterInit annotated methods...
                constructorParameterList.add(newComponent);
            }
        }

        if (constructorParameterList.size() == constructor.getParameterCount()) {
            // since the parameter count we collected is equal to the constructors parameter count,
            // we know that this arrangement is correct and can continue to invoke a new instance...

            // store this newly created component in a variable for heap analysis
            final T component = constructor.newInstance(constructorParameterList.toArray());

            return component;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Calls all defined @AfterInit method on the given object.
     *
     * @param object The object.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void callAfterInitMethods(@NonNull Object object) throws InvocationTargetException, IllegalAccessException {
        // iterate over every method and call any annotated with @AfterInit...
        for (Method method : object.getClass().getMethods()) {
            if (!method.isAnnotationPresent(AfterInit.class)) {
                continue;
            }

            final List<Object> injectedParamList = new ArrayList<>();

            for (Parameter parameter : method.getParameters()) {
                final Object instance = getInstance(parameter.getType());

                injectedParamList.add(instance);
            }

            // method is annotated with @AfterInit, invoke method using component object instance.
            method.invoke(object, injectedParamList.toArray());
        }
    }

    private static <T> Optional<Class<? extends T>> getImplementation(@NonNull Class<T> type) {
        return new Reflections(EssentiaInject.getPackageNameList()).getSubTypesOf(type).stream()
                .findFirst();
    }
}
