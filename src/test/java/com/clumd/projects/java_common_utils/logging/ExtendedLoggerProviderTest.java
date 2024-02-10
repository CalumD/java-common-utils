package com.clumd.projects.java_common_utils.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExtendedLoggerProviderTest {

    @Test
    void test_loggerFactory_requestedApiVersion() {
        assertEquals("2.0.99", new ExtendedLoggerProvider().getRequestedApiVersion());
    }

    @Test
    void test_loggerFactory_starts_out_null() {
        assertNull(new ExtendedLoggerProvider().getLoggerFactory());
    }

    @Test
    void test_markerFactory_starts_out_null() {
        assertNull(new ExtendedLoggerProvider().getMarkerFactory());
    }

    @Test
    void test_MDCAdapter_starts_out_null() {
        assertNull(new ExtendedLoggerProvider().getMDCAdapter());
    }

    @Test
    void test_loggerFactory_ends_populated() {
        ExtendedLoggerProvider extendedLoggerProvider = new ExtendedLoggerProvider();
        extendedLoggerProvider.initialize();

        assertNotNull(extendedLoggerProvider.getLoggerFactory());
    }

    @Test
    void test_markerFactory_ends_populated() {
        ExtendedLoggerProvider extendedLoggerProvider = new ExtendedLoggerProvider();
        extendedLoggerProvider.initialize();

        assertNotNull(extendedLoggerProvider.getMarkerFactory());
    }

    @Test
    void test_MDCAdapter_ends_populated() {
        ExtendedLoggerProvider extendedLoggerProvider = new ExtendedLoggerProvider();
        extendedLoggerProvider.initialize();

        assertNotNull(extendedLoggerProvider.getMDCAdapter());
    }
}
