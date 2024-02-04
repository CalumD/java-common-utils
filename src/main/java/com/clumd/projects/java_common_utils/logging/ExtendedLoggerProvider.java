package com.clumd.projects.java_common_utils.logging;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * Basically directly copied from {@code org.slf4j.simple.SimpleServiceProvider} but for my {@link ExtendedLogger}s
 */
public class ExtendedLoggerProvider implements SLF4JServiceProvider {

    // to avoid constant folding by the compiler, this field must *not* be final
    @SuppressWarnings("java:S1444")
    public static String requestedApiVersion = "2.0.99"; // !final

    private ILoggerFactory loggerFactory;
    private IMarkerFactory markerFactory;
    private MDCAdapter mdcAdapter;

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return requestedApiVersion;
    }

    @Override
    public void initialize() {
        loggerFactory = new ExtendedLoggerFactory();
        markerFactory = new BasicMarkerFactory();
        mdcAdapter = new NOPMDCAdapter();
    }
}
