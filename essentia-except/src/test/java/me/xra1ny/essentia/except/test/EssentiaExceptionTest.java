package me.xra1ny.essentia.except.test;

import me.xra1ny.essentia.except.test.exception.TestException;
import me.xra1ny.except.EssentiaExcept;

public class EssentiaExceptionTest {
    public static void main(String[] args) throws TestException {
        EssentiaExcept.run(EssentiaExceptionTest.class.getPackageName());

        throw new TestException();
    }
}
