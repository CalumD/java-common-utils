package com.clumd.projects.java_common_utils.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ExtendedLoggerFactoryTest {


    @Test
    void test_getLogger_getsNewExtendedLogger() throws InterruptedException {

        Thread t = new Thread(
                Thread.currentThread().getThreadGroup(),
                () -> {
                    ExtendedSlf4jLogger newLogger = new ExtendedLoggerFactory().getLogger("testing logger");
                    assertNotNull(newLogger);
                },
                "dodge polluting the main thread space."
        );

        t.start();
        t.join(1000L);
    }

    @Test
    void test_getLogger_returns_an_already_existing_logger() throws InterruptedException {

        Thread t = new Thread(
                Thread.currentThread().getThreadGroup(),
                () -> {
                    ExtendedSlf4jLogger firstLogger = new ExtendedLoggerFactory().getLogger("one");
                    assertSame(firstLogger, new ExtendedLoggerFactory().getLogger("one"));
                },
                "dodge polluting the main thread space."
        );

        t.start();
        t.join(1000L);
    }
}
