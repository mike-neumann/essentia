package me.xra1ny.essentia.configs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for custom mapped complex config objects.
 *
 * @apiNote Using this annotation on a config object, all @Property annotated fields will be mapped according to their mapped string value in config.
 * @author xRa1ny
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigObject {
}
