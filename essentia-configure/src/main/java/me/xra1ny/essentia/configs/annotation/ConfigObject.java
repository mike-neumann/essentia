package me.xra1ny.essentia.configs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for custom complex config objects.
 *
 * @apiNote Using this annotation allows YAML to be parsed with generic type declarations supported. e.g. List<T></T>
 * @author xRa1ny
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigObject {
}
