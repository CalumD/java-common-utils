package com.clumd.projects.java_common_utils.logging;

import lombok.Getter;
import lombok.NonNull;

import static com.clumd.projects.java_common_utils.logging.LevelFormat.*;

import java.io.Serializable;
import java.util.List;

public class LogLevel implements Serializable {

    public static final String COLOUR_RESET = "\033[0m";

    public static final LogLevel ALL = LogLevel.of("ALL", Integer.MIN_VALUE);
    public static final LogLevel OFF = LogLevel.of("OFF", Integer.MAX_VALUE);

    public static final LogLevel FATAL = LogLevel.of("FATAL", 5000, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final LogLevel CRITICAL = LogLevel.of("CRITICAL", 4000, LevelFormat.YELLOW);
    public static final LogLevel SEVERE = LogLevel.of("SEVERE", 3000, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final LogLevel WARNING = LogLevel.of("WARNING", 2000, LevelFormat.RED);
    public static final LogLevel IMPORTANT = LogLevel.of("IMPORTANT", 1000, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final LogLevel INFO = LogLevel.of("INFO", 0, LevelFormat.GREEN);
    public static final LogLevel CONFIG = LogLevel.of("CONFIG", -1000, LevelFormat.PURPLE);
    public static final LogLevel VERBOSE = LogLevel.of("VERBOSE", -2000, LevelFormat.BLUE);
    public static final LogLevel DEBUG = LogLevel.of("DEBUG", -3000, LevelFormat.CYAN);
    public static final LogLevel TRACE = LogLevel.of("TRACE", -4000, LevelFormat.WHITE);

    private final String level;
    @Getter
    private final int priority;
    @Getter
    private final String levelFormat;

    public LogLevel(@NonNull String level, int priority) {
        this.level = level;
        this.priority = priority;
        this.levelFormat = null;
    }

    public LogLevel(@NonNull String level, int priority, @NonNull final String levelFormat) {
        this.level = level;
        this.priority = priority;
        this.levelFormat = levelFormat;
    }

    public LogLevel(@NonNull String level, int priority, @NonNull final LevelFormat levelFormat) {
        this(level, priority, levelFormat.getFormatString());
    }

    static LogLevel of(@NonNull final String level, final int priority) {
        return new LogLevel(level, priority);
    }

    static LogLevel of(@NonNull final String level, final int priority, @NonNull final String levelFormat) {
        return new LogLevel(level, priority, levelFormat);
    }

    static LogLevel of(@NonNull final String level, final int priority, @NonNull final LevelFormat levelFormat) {
        return new LogLevel(level, priority, levelFormat);
    }

    public String getLevel() {
        return toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof LogLevel otherLogLevel) {
            return otherLogLevel.priority == this.priority;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.priority);
    }

    @Override
    public String toString() {
        return level.toUpperCase();
    }
}
