package me.xra1ny.essentia.configs.annotation;

import me.xra1ny.essentia.configs.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

/**
 * Defines a field within an {@link Config} extending class to be a key.
 *
 * @author xRa1ny
 * @apiNote Not to be confused with .properties files, this annotation is also used for every other config type supported by essentia.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    /**
     * Defines the class types this annotated field manages.
     *
     * @return The classes this annotated field manages.
     * @apiNote When annotating a {@link List} or {@link Map}, specify their generic types.
     */
    Class<?>[] value();
}