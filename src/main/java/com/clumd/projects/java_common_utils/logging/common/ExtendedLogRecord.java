package com.clumd.projects.java_common_utils.logging.common;

import lombok.Getter;
import lombok.NonNull;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ExtendedLogRecord extends LogRecord {

    @Getter
    private Set<String> tags;

    public ExtendedLogRecord(Level level, String msg) {
        super(level, msg);
    }

    public ExtendedLogRecord(Level level, String msg, @NonNull Set<String> tags) {
        this(level, msg);
        this.tags = tags;
    }

    public ExtendedLogRecord(Level level, String msg, @NonNull String tag) {
        this(level, msg, Set.of(tag));
    }
}
