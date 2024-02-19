package me.xra1ny.essentia.configs.annotation;

import me.xra1ny.essentia.configs.Config;
import me.xra1ny.essentia.configs.processor.FileProcessor;
import me.xra1ny.essentia.configs.processor.PropertiesFileProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines meta-information for the annotated {@link Config}.
 *
 * @author xRa1ny
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigInfo {
    /**
     * Defines the file name for the annotated {@link Config}.
     *
     * @apiNote Includes the file extension (e.g test.yml; test.properties)
     * @return The file name
     */
    String value();

    /**
     * Defines the {@link FileProcessor} used by this config.
     *
     * @apiNote {@link FileProcessor}s are used to process config files.
     * @return The processor used by this config.
     */
    Class<? extends FileProcessor> processor() default PropertiesFileProcessor.class;
}
