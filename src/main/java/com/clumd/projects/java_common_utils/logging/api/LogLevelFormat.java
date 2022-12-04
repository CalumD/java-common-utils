package com.clumd.projects.java_common_utils.logging.api;

import java.io.Serializable;

/**
 * Used as a marker interface for the Formatting of an extended LogLevel
 */
public interface LogLevelFormat extends Serializable {

    /**
     * Gets the formatting string for the associated Log level
     *
     * @return The Escape codes for the associated Log Level.
     */
    String getFormatString();
}
