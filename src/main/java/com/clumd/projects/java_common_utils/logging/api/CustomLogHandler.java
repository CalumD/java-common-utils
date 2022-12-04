package com.clumd.projects.java_common_utils.logging.api;

import lombok.NonNull;

import java.util.Map;
import java.util.UUID;

/**
 * An Interface to ensure that a custom Log Controller instance is capable of accepting some variables which should
 * included in logs.
 */
public interface CustomLogHandler {

    /**
     * A method to act as an initialiser for any Custom Log Controller
     *
     * @param specificRunID         A unique identifier for any given specific 'run' of the system.
     * @param systemID              An identifier for the hostname of the hardware running this instance.
     * @param overriddenThreadNames A map between ThreadIDs and their human-readable names, perhaps describing what they
     *                              do.
     */
    void acceptLogRootRefs(@NonNull final UUID specificRunID, @NonNull final String systemID, @NonNull final Map<Long, String> overriddenThreadNames);
}
