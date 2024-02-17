package me.xra1ny.except;

import lombok.Getter;
import lombok.NonNull;
import me.xra1ny.except.annotation.ExceptionHandler;
import me.xra1ny.except.exception.ExceptionHandlerMethodInvalidSignatureException;
import me.xra1ny.except.exception.ExceptionHandlerMethodNotStaticException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * EssentiaExcept's main class, used to initialize EssentiaExcept.
 *
 * @author xRa1ny
 */
public class EssentiaExcept {
    @Getter
    @NonNull
    private static final List<String> packageNameList = new ArrayList<>();

    private static List<Method> getExceptionHandlerMethods() {
        return new Reflections(packageNameList, new MethodAnnotationsScanner()).getMethodsAnnotatedWith(ExceptionHandler.class).stream()
                .filter(method -> {
                    if(!Modifier.isStatic(method.getModifiers())) {
                        throw new ExceptionHandlerMethodNotStaticException(method);
                    }

                    final ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);

                    if(method.getParameterCount() != 1 && !method.getParameterTypes()[0].equals(exceptionHandler.value())) {
                        throw new ExceptionHandlerMethodInvalidSignatureException(method);
                    }


                    return true;
                })
                .toList();
    }

    public static void run(@NonNull String @NonNull ... packageNames) {
        packageNameList.addAll(List.of(packageNames));

        final List<Method> exceptionHandlerMethodsList = getExceptionHandlerMethods();

        Thread.currentThread().setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(exceptionHandlerMethodsList));
    }

    public static void run(@NonNull Logger logger, @NonNull String @NonNull ... packageNames) {
        packageNameList.addAll(List.of(packageNames));

        final List<Method> exceptionHandlerMethodsList = getExceptionHandlerMethods();

        logger.addHandler(new LoggerUncaughtExceptionHandler(exceptionHandlerMethodsList));
    }
}
