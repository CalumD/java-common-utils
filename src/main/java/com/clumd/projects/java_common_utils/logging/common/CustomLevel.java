package com.clumd.projects.java_common_utils.logging.common;

import com.clumd.projects.java_common_utils.logging.api.LogLevel;
import com.clumd.projects.java_common_utils.logging.api.LogLevelFormat;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import static com.clumd.projects.java_common_utils.logging.common.Format.*;

public class CustomLevel extends Level implements LogLevel, Serializable {

    public static final String COLOUR_RESET = "\033[0m";

    public static final CustomLevel ALL = CustomLevel.of("ALL", Integer.MIN_VALUE, RESET);
    public static final CustomLevel OFF = CustomLevel.of("OFF", Integer.MAX_VALUE, RESET);
    public static final CustomLevel NONE = CustomLevel.of("NONE", Integer.MAX_VALUE, RESET);

    public static final CustomLevel SHUTDOWN = CustomLevel.of("SHUTDOWN", 1050, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final CustomLevel EMERGENCY = CustomLevel.of("EMERGENCY", 1050, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final CustomLevel FATAL = CustomLevel.of("FATAL", 1050, createFormat(List.of(BOLD, BRIGHT_YELLOW)));
    public static final CustomLevel CRITICAL = CustomLevel.of("CRITICAL", 1000, BRIGHT_YELLOW);
    public static final CustomLevel SEVERE = CustomLevel.of("SEVERE", 1000, BRIGHT_YELLOW);
    public static final CustomLevel ERROR = CustomLevel.of("ERROR", 950, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final CustomLevel FAILURE = CustomLevel.of("FAILURE", 950, createFormat(List.of(BOLD, BRIGHT_RED)));
    public static final CustomLevel WARNING = CustomLevel.of("WARNING", 900, RED);
    public static final CustomLevel IMPORTANT = CustomLevel.of("IMPORTANT", 850, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final CustomLevel NOTIFICATION = CustomLevel.of("NOTIFICATION", 850, createFormat(List.of(BOLD, BRIGHT_GREEN)));
    public static final CustomLevel INFO = CustomLevel.of("INFO", 800, GREEN);
    public static final CustomLevel SUCCESS = CustomLevel.of("SUCCESS", 800, GREEN);
    public static final CustomLevel DATA = CustomLevel.of("DATA", 700, PURPLE);
    public static final CustomLevel CONFIG = CustomLevel.of("CONFIG", 700, PURPLE);
    public static final CustomLevel VERBOSE = CustomLevel.of("VERBOSE", 500, BLUE);
    public static final CustomLevel MINOR = CustomLevel.of("MINOR", 500, BLUE);
    public static final CustomLevel DEBUG = CustomLevel.of("DEBUG", 400, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final CustomLevel TESTING = CustomLevel.of("TESTING", 400, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final CustomLevel TRACE = CustomLevel.of("TRACE", 300, WHITE);

    private static final Map<String, CustomLevel> ALL_LEVELS = Map.ofEntries(
            Map.entry(ALL.getLevelName(), ALL),
            Map.entry(OFF.getLevelName(), OFF),
            Map.entry(NONE.getLevelName(), NONE),
            Map.entry(SHUTDOWN.getLevelName(), SHUTDOWN),
            Map.entry(EMERGENCY.getLevelName(), EMERGENCY),
            Map.entry(FATAL.getLevelName(), FATAL),
            Map.entry(CRITICAL.getLevelName(), CRITICAL),
            Map.entry(SEVERE.getLevelName(), SEVERE),
            Map.entry(ERROR.getLevelName(), ERROR),
            Map.entry(FAILURE.getLevelName(), FAILURE),
            Map.entry(WARNING.getLevelName(), WARNING),
            Map.entry(IMPORTANT.getLevelName(), IMPORTANT),
            Map.entry(NOTIFICATION.getLevelName(), NOTIFICATION),
            Map.entry(INFO.getLevelName(), INFO),
            Map.entry(SUCCESS.getLevelName(), SUCCESS),
            Map.entry(CONFIG.getLevelName(), CONFIG),
            Map.entry(DATA.getLevelName(), DATA),
            Map.entry(VERBOSE.getLevelName(), VERBOSE),
            Map.entry(MINOR.getLevelName(), MINOR),
            Map.entry(DEBUG.getLevelName(), DEBUG),
            Map.entry(TESTING.getLevelName(), TESTING),
            Map.entry(TRACE.getLevelName(), TRACE)
    );

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
    }

    public CustomLevel(@NonNull String level, int priority, @NonNull final String levelFormat) {
        super(level.toUpperCase(), priority);
        this.levelName = this.toString();
        this.priority = priority;
        this.levelFormat = levelFormat;
    }

    public CustomLevel(@NonNull String level, int priority, @NonNull final LogLevelFormat format) {
        this(level, priority, format.getFormatString());
    }

    public static CustomLevel of(@NonNull final String level, final int priority) {
        return new CustomLevel(level, priority);
    }

    public static CustomLevel of(@NonNull final String level, final int priority, @NonNull final String levelFormat) {
        return new CustomLevel(level, priority, levelFormat);
    }

    public static CustomLevel of(@NonNull final String level, final int priority, @NonNull final LogLevelFormat format) {
        return new CustomLevel(level, priority, format);
    }

    public static CustomLevel parse(String thing) {
        CustomLevel ret = ALL_LEVELS.get(thing.toUpperCase());
        if (ret == null) {
            Level boringLevel = Level.parse(thing.toUpperCase());
            if (boringLevel instanceof CustomLevel extendedLevel) {
                return extendedLevel;
            }
            return CustomLevel.of(boringLevel.getName(), boringLevel.intValue());
        }
        return ret;
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
        if (other instanceof Level julLevel) {
            return julLevel.intValue() == this.priority || Objects.equals(julLevel.getName(), this.levelName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Integer.hashCode(this.priority), this.levelName.hashCode());
    }
}
