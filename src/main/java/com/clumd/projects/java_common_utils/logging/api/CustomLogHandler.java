package com.clumd.projects.java_common_utils.logging.api;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public interface CustomLogHandler {
    CustomLogHandler acceptLogRootRefs(UUID specificRunId, Level startingLevel, String systemId, Map<Integer, String> overriddenThreadNameMappings);
}
