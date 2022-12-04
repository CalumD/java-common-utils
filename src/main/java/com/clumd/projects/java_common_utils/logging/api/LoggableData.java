package com.clumd.projects.java_common_utils.logging.api;

/**
 * This interface should be used to indicate that a class implements a method to format itself in a design suitable for
 * logging.
 * <p>
 * This may be useful to either obfuscate, or even totally remove certain information from a class which would otherwise
 * be exposed, or just to make the output formatting/layout of an object be more palatable when reading it in a log.
 */
public interface LoggableData {

    /**
     * Used to return the data the implementing class would like to display within a Log statement.
     *
     * @return The data for the log formatted how you would like it to be displayed within a console.
     */
    default String getFormattedLogData() {
        return this.toString();
    }
}
