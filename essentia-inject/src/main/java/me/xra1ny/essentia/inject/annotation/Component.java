package me.xra1ny.essentia.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a singular dependency injectable component that should be auto registered when running me.xra1ny.essentia.inject.EssentiaInject.
 *
 * @author xRa1ny
 * @apiNote All components are singleton objects, and are only created one time.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * Specifies any dependencies for this annotated class member.
     *
     * @return The dependencies of this annotated class member.
     * @apiNote Any specified dependencies will be initialised before the annotated member is initialised.
     */
    Class<?>[] dependsOn() default {};

    /**
     * Specifies any dependencies to initialise after the annotated member is initialised.
     *
     * @return The dependencies to initialise after the annotated member.
     * @apiNote Any specified classes will be initialised AFTER the annotated member is.
     */
    Class<?>[] after() default {};
}