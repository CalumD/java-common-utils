package com.clumd.projects.java_common_utils.logging;

import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import static com.clumd.projects.java_common_utils.logging.Format.*;

public class CustomLevel extends Level implements LogLevel, Serializable {

    private static final Map<Level, LogLevel> ALL_LEVELS = new HashMap<>();

    public static final String COLOUR_RESET = "\033[0m";

    public static final CustomLevel ALL = CustomLevel.of("ALL", Integer.MIN_VALUE, RESET);
    public static final CustomLevel OFF = CustomLevel.of("OFF", Integer.MAX_VALUE, RESET);

    public static final CustomLevel SHUTDOWN = CustomLevel.of("SHUTDOWN", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final CustomLevel EMERGENCY = CustomLevel.of("EMERGENCY", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final CustomLevel FATAL = CustomLevel.of("FATAL", 50, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final CustomLevel CRITICAL = CustomLevel.of("CRITICAL", 40, YELLOW);
    public static final CustomLevel SEVERE = CustomLevel.of("SEVERE", 40, YELLOW);
    public static final CustomLevel ERROR = CustomLevel.of("ERROR", 30, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final CustomLevel FAILURE = CustomLevel.of("FAILURE", 30, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final CustomLevel WARNING = CustomLevel.of("WARNING", 20, RED);
    public static final CustomLevel IMPORTANT = CustomLevel.of("IMPORTANT", 10, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final CustomLevel NOTIFICATION = CustomLevel.of("NOTIFICATION", 10, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final CustomLevel INFO = CustomLevel.of("INFO", 0, GREEN);
    public static final CustomLevel SUCCESS = CustomLevel.of("SUCCESS", 0, GREEN);
    public static final CustomLevel CONFIG = CustomLevel.of("CONFIG", -10, PURPLE);
    public static final CustomLevel DATA = CustomLevel.of("DATA", -10, PURPLE);
    public static final CustomLevel VERBOSE = CustomLevel.of("VERBOSE", -20, BLUE);
    public static final CustomLevel MINOR = CustomLevel.of("MINOR", -20, BLUE);
    public static final CustomLevel DEBUG = CustomLevel.of("DEBUG", -30, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final CustomLevel TESTING = CustomLevel.of("TESTING", -30, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final CustomLevel TRACE = CustomLevel.of("TRACE", -40, WHITE);

    @Getter
    private final String levelName;
    @Getter
    private final int priority;
    @Getter
    private final String levelFormat;

    public CustomLevel(@NonNull String level, int priority) {
        super(level.toUpperCase(), priority);
        this.levelName = this.toString();
        this.priority = priority;
        this.levelFormat = null;
        ALL_LEVELS.put(this, this);
    }

    public CustomLevel(@NonNull String level, int priority, @NonNull final String levelFormat) {
        super(level.toUpperCase(), priority);
        this.levelName = this.toString();
        this.priority = priority;
        this.levelFormat = levelFormat;
        ALL_LEVELS.put(this, this);
    }

    public CustomLevel(@NonNull String level, int priority, @NonNull final LogLevelFormat format) {
        this(level, priority, format.getFormatString());
    }

    static CustomLevel of(@NonNull final String level, final int priority) {
        return new CustomLevel(level, priority);
    }

    static CustomLevel of(@NonNull final String level, final int priority, @NonNull final String levelFormat) {
        return new CustomLevel(level, priority, levelFormat);
    }

    static CustomLevel of(@NonNull final String level, final int priority, @NonNull final LogLevelFormat format) {
        return new CustomLevel(level, priority, format);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof CustomLevel otherLevel) {
            return otherLevel.priority == this.priority && Objects.equals(otherLevel.levelName, this.levelName);
        }
        return false;
    }

    public boolean weakEquals(final Object other) {
        if (equals(other)) {
            return true;
        }
        if (other instanceof CustomLevel otherLevel) {
            return otherLevel.priority == this.priority || Objects.equals(otherLevel.levelName, this.levelName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Integer.hashCode(this.priority), this.levelName.hashCode());
    }
}
