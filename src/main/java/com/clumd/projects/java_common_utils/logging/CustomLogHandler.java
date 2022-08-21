package com.clumd.projects.java_common_utils.logging;

import java.util.Map;
import java.util.UUID;

public interface CustomLogHandler {
    void acceptLogRootRefs(UUID specificRunId, String systemId, Map<Integer, String> overriddenThreadNameMappings);

}
