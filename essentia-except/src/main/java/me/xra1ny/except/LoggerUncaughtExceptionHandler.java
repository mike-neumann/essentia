package me.xra1ny.except;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.LogRecord;

public class LoggerUncaughtExceptionHandler extends UncaughtExceptionHandler {

    public LoggerUncaughtExceptionHandler(@NonNull List<Method> exceptionHandlerMethodList) {
        super(exceptionHandlerMethodList);
    }

    @Override
    public void publish(LogRecord record) {
        if (record.getThrown() == null) {
            return;
        }

        handle(record.getThrown());
    }
}
