package com.clumd.projects.java_common_utils.logging;

import org.slf4j.ILoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.LogManager;

/**
 * Basically directly copied from {@code org.slf4j.simple.SimpleLoggerFactory} but for my {@link ExtendedLogger}s
 */
public class ExtendedLoggerFactory implements ILoggerFactory {

    private final ConcurrentMap<String, ExtendedSlf4jLogger> loggerMap = new ConcurrentHashMap<>();

    @Override
    public ExtendedSlf4jLogger getLogger(String name) {

        ExtendedSlf4jLogger existingLogger = loggerMap.get(name);

        if (existingLogger != null) {
            return existingLogger;
        }

        // Create a logger under com.clumd version & keep track of it
        ExtendedSlf4jLogger newInstance = new ExtendedSlf4jLogger(LogRoot.buildLogName(null, name));
        ExtendedSlf4jLogger reference = loggerMap.putIfAbsent(name, newInstance);

        // Integrate this new logger into the com.clumd management
        LogManager.getLogManager().addLogger(newInstance);
        LogRoot.updateThreadIdName(Thread.currentThread().threadId(), Thread.currentThread().getName());

        return reference;
    }
}
