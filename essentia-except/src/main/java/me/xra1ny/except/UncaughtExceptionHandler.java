package me.xra1ny.except;

import lombok.NonNull;
import me.xra1ny.except.annotation.ExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class UncaughtExceptionHandler extends Handler {
    @NonNull
    private final List<Method> exceptionHandlerMethodList;

    public UncaughtExceptionHandler(@NonNull List<Method> exceptionHandlerMethodList) {
        this.exceptionHandlerMethodList = exceptionHandlerMethodList;
    }

    public void handle(@NonNull Throwable e) {
        final List<Method> exceptionHandlerMethodList = getMethodsByHandledException(e);

        exceptionHandlerMethodList
                .forEach(method -> {
                    try {
                        method.invoke(this, e);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        e.printStackTrace();
                        throw new RuntimeException("error while invoking exception handler method %s"
                                .formatted(method.getName()));
                    }
                });
    }

    private List<Method> getMethodsByHandledException(@NonNull Throwable e) {
        return exceptionHandlerMethodList.stream()
                .filter(method -> e.getClass().isAssignableFrom(method.getAnnotation(ExceptionHandler.class).value()))
                .toList();
    }

    @Override
    public void publish(LogRecord record) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}