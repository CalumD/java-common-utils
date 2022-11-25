package com.clumd.projects.java_common_utils.logging.api;

import lombok.NonNull;

import java.util.Map;
import java.util.UUID;

public interface CustomLogController {

    void acceptLogRootRefs(@NonNull final UUID specificRunID, @NonNull final String systemID, @NonNull final Map<Long, String> overriddenThreadNames);

}
