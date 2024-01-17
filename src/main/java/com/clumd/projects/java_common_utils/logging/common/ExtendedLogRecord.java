package com.clumd.projects.java_common_utils.logging.common;

import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * An extended {@link LogRecord} which also supports the notion of 'tagging' log messages.
 * <p>
 * Tagging could be used to cross-reference which could be related, but come from totally distinct loggers. For EXAMPLE,
 * tagging with 'Security', but for messages coming from some User Input validation, vs some Socket level communication,
 * which could then be pulled out or used as an Index in some log aggregation.
 * <p>
 * Also, with reference to so-called "bakedInTags", these should point to an additional pre-initialised, ideally Immutable Set of String tags.
 * These additional tags should be stamped along-side any other potentially provided tags and tends to be most useful for things such as
 * distributed compute trace IDs or things such as concrete / unchanging environment variables.
 */
@Getter
public class ExtendedLogRecord extends LogRecord {

    private Set<String> tags;
    private Set<String> bakedInTags;
    private Set<Class<? extends StreamHandler>> controllersWhichShouldDisregardThisMessage;

    public ExtendedLogRecord(Level level, String msg) {
        super(level, msg);
        Optional<CustomLevel> custom = CustomLevel.convertJulEquivalent(level);
        custom.ifPresent(this::setLevel);
    }

    public ExtendedLogRecord(Level level, String msg, @NonNull Set<String> tags) {
        this(level, msg);
        this.tags = tags;
    }

    public ExtendedLogRecord(Level level, String msg, @NonNull String tag) {
        this(level, msg, Set.of(tag));
    }

    public ExtendedLogRecord referencingBakedInTags(final Set<String> bakedInTags) {
        this.bakedInTags = bakedInTags;
        return this;
    }

    public ExtendedLogRecord withControllersWhichShouldIgnore(final Set<Class<? extends StreamHandler>> controllersWhichShouldDisregardThisMessage) {
        this.controllersWhichShouldDisregardThisMessage = controllersWhichShouldDisregardThisMessage;
        return this;
    }
}
