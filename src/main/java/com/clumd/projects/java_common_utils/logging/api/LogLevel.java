package com.clumd.projects.java_common_utils.logging.api;

import java.io.Serializable;

/**
 * Define a custom extended LogLevel which should always extend {@link java.util.logging.Level}, but provide some
 * additional fields and functionality.
 */
public interface LogLevel extends Serializable {

    /**
     * Get the name of this Log Level.
     *
     * @return The Name of this Custom Log Level
     */
    String getLevelName();

    /**
     * Get the priority of this Log Level
     *
     * @return The priority of this Log Level
     */
    int getPriority();

    /**
     * Get the formatting this Log Level should be displayed with
     *
     * @return The escape codes required to display log messages at this Log Level.
     */
    String getLevelFormat();
}
