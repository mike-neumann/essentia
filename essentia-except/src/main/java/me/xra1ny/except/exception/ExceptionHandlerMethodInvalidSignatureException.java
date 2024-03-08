package me.xra1ny.except.exception;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ExceptionHandlerMethodInvalidSignatureException extends RuntimeException {
    public ExceptionHandlerMethodInvalidSignatureException(@NonNull Method method) {
        super("invalid signature %s for method %s"
                .formatted(Arrays.toString(method.getParameterTypes()), method.getName()));
    }
}
