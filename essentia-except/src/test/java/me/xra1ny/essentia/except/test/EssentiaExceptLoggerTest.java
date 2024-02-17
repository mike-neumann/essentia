package me.xra1ny.essentia.except.test;

import me.xra1ny.essentia.except.test.exception.TestException;
import me.xra1ny.except.EssentiaExcept;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class EssentiaExceptLoggerTest {
    public static void main(String[] args) throws TestException {
        final Logger logger = Logger.getLogger(EssentiaExceptLoggerTest.class.getSimpleName());

        EssentiaExcept.run(logger, EssentiaExceptLoggerTest.class.getPackageName());

        final LogRecord logRecord = new LogRecord(Level.SEVERE, "Exception");

        logRecord.setThrown(new TestException());

        logger.log(logRecord);
    }
}
