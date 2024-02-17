package me.xra1ny.essentia.except.test.handler;

import lombok.extern.java.Log;
import me.xra1ny.essentia.except.test.exception.TestException;
import me.xra1ny.except.annotation.ExceptionHandler;

@Log
public class TestExceptionHandler {
    @ExceptionHandler(TestException.class)
    public static void handleTestException1(TestException e) {
        log.info("caught test exception1!");
    }

    @ExceptionHandler(TestException.class)
    public static void handleTestException2(TestException e) {
        log.info("caught test exception2!");
    }

    @ExceptionHandler(TestException.class)
    public static void handleTestException3(TestException e) {
        log.info("caught test exception3!");
    }

}
