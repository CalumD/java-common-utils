package com.clumd.projects.java_common_utils.logging.common;

import lombok.Getter;
import lombok.NonNull;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * An extended {@link LogRecord} which also supports the notion of 'tagging' log messages.
 * <p></p>
 * Tagging could be used to cross-reference which could be related, but come from totally distinct loggers. For EXAMPLE,
 * tagging with 'Security', but for messages coming from some User Input validation, vs some Socket level communication,
 * which could then be pulled out or used as an Index in some log aggregation.
 */
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
