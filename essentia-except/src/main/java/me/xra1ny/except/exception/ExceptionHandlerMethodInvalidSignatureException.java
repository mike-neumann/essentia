package me.xra1ny.except.exception;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ExceptionHandlerMethodInvalidSignatureException extends RuntimeException {
    public ExceptionHandlerMethodInvalidSignatureException(@NonNull Method method) {
        super("invalid signature " + Arrays.toString(method.getParameterTypes()) + " for method " + method.getName());
    }
}
