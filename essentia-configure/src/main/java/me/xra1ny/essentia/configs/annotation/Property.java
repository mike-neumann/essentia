package me.xra1ny.essentia.configs.annotation;

import me.xra1ny.essentia.configs.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
     * Defines the config key under which this annotated field is mapped.
     *
     * @return The config key.
     */
    String value();
}
