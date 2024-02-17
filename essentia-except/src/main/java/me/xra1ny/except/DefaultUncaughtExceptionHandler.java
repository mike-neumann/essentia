package me.xra1ny.except;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.List;

public class DefaultUncaughtExceptionHandler extends UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    public DefaultUncaughtExceptionHandler(@NonNull List<Method> exceptionHandlerMethodList) {
        super(exceptionHandlerMethodList);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handle(e);
    }
}
