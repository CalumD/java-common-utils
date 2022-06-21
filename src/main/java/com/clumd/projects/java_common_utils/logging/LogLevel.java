package com.clumd.projects.java_common_utils.logging;

import lombok.NonNull;

public record LogLevel(@NonNull String level, int priority, String levelFormat) {

    static LogLevel of(@NonNull final String level, final int priority, final String levelFormat) {
        return new LogLevel(level, priority, levelFormat);
    }
}
