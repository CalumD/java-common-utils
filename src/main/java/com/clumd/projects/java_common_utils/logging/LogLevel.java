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

    public static final LogLevel SHUTDOWN = LogLevel.of("SHUTDOWN", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final LogLevel EMERGENCY = LogLevel.of("EMERGENCY", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final LogLevel FATAL = LogLevel.of("FATAL", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final LogLevel CRITICAL = LogLevel.of("CRITICAL", 40, YELLOW);
    public static final LogLevel SEVERE = LogLevel.of("SEVERE", 40, YELLOW);
    public static final LogLevel ERROR = LogLevel.of("ERROR", 30, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final LogLevel FAILURE = LogLevel.of("FAILURE", 30, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final LogLevel WARNING = LogLevel.of("WARNING", 20, RED);
    public static final LogLevel IMPORTANT = LogLevel.of("IMPORTANT", 10, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final LogLevel NOTIFICATION = LogLevel.of("NOTIFICATION", 10, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final LogLevel INFO = LogLevel.of("INFO", 0, GREEN);
    public static final LogLevel SUCCESS = LogLevel.of("SUCCESS", 0, GREEN);
    public static final LogLevel CONFIG = LogLevel.of("CONFIG", -10, PURPLE);
    public static final LogLevel DATA = LogLevel.of("DATA", -10, PURPLE);
    public static final LogLevel VERBOSE = LogLevel.of("VERBOSE", -20, BLUE);
    public static final LogLevel MINOR = LogLevel.of("MINOR", -20, BLUE);
    public static final LogLevel DEBUG = LogLevel.of("DEBUG", -30, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final LogLevel TESTING = LogLevel.of("TESTING", -30, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final LogLevel TRACE = LogLevel.of("TRACE", -40, WHITE);


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
