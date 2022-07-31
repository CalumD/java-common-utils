package com.clumd.projects.java_common_utils.logging;

import lombok.Getter;
import lombok.NonNull;

import static com.clumd.projects.java_common_utils.logging.Format.*;

import java.io.Serializable;
import java.util.List;

public class Level implements LogLevel, Serializable {

    public static final String COLOUR_RESET = "\033[0m";

    public static final Level ALL = Level.of("ALL", Integer.MIN_VALUE);
    public static final Level OFF = Level.of("OFF", Integer.MAX_VALUE);

    public static final Level SHUTDOWN = Level.of("SHUTDOWN", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final Level EMERGENCY = Level.of("EMERGENCY", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final Level FATAL = Level.of("FATAL", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final Level CRITICAL = Level.of("CRITICAL", 40, YELLOW);
    public static final Level SEVERE = Level.of("SEVERE", 40, YELLOW);
    public static final Level ERROR = Level.of("ERROR", 30, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final Level FAILURE = Level.of("FAILURE", 30, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final Level WARNING = Level.of("WARNING", 20, RED);
    public static final Level IMPORTANT = Level.of("IMPORTANT", 10, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final Level NOTIFICATION = Level.of("NOTIFICATION", 10, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final Level INFO = Level.of("INFO", 0, GREEN);
    public static final Level SUCCESS = Level.of("SUCCESS", 0, GREEN);
    public static final Level CONFIG = Level.of("CONFIG", -10, PURPLE);
    public static final Level DATA = Level.of("DATA", -10, PURPLE);
    public static final Level VERBOSE = Level.of("VERBOSE", -20, BLUE);
    public static final Level MINOR = Level.of("MINOR", -20, BLUE);
    public static final Level DEBUG = Level.of("DEBUG", -30, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final Level TESTING = Level.of("TESTING", -30, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final Level TRACE = Level.of("TRACE", -40, WHITE);


    private final String levelName;
    @Getter
    private final int priority;
    @Getter
    private final String levelFormat;

    public Level(@NonNull String level, int priority) {
        this.levelName = level;
        this.priority = priority;
        this.levelFormat = null;
    }

    public Level(@NonNull String level, int priority, @NonNull final String levelFormat) {
        this.levelName = level;
        this.priority = priority;
        this.levelFormat = levelFormat;
    }

    public Level(@NonNull String level, int priority, @NonNull final LogLevelFormat format) {
        this(level, priority, format.getFormatString());
    }

    static Level of(@NonNull final String level, final int priority) {
        return new Level(level, priority);
    }

    static Level of(@NonNull final String level, final int priority, @NonNull final String levelFormat) {
        return new Level(level, priority, levelFormat);
    }

    static Level of(@NonNull final String level, final int priority, @NonNull final LogLevelFormat format) {
        return new Level(level, priority, format);
    }

    @Override
    public String getLevelName() {
        return toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Level otherLevel) {
            return otherLevel.priority == this.priority;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.priority);
    }

    @Override
    public String toString() {
        return levelName.toUpperCase();
    }
}
