package com.clumd.projects.java_common_utils.logging.common;

import com.clumd.projects.java_common_utils.logging.api.LogLevel;
import com.clumd.projects.java_common_utils.logging.api.LogLevelFormat;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

import static com.clumd.projects.java_common_utils.logging.common.Format.*;

@Getter
public class CustomLevel extends Level implements LogLevel, Serializable {

    private static final Map<String, CustomLevel> ALL_LEVELS = new HashMap<>(); // This will be populated by each static CustomLevel.of() call in the following lines.

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
    public static final CustomLevel FINE = CustomLevel.of("FINE", 500, BLUE);
    public static final CustomLevel MINOR = CustomLevel.of("MINOR", 500, BLUE);
    public static final CustomLevel DEBUG = CustomLevel.of("DEBUG", 400, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final CustomLevel FINER = CustomLevel.of("FINER", 400, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final CustomLevel TESTING = CustomLevel.of("TESTING", 400, createFormat(List.of(BOLD, CYAN, OUTLINE)));
    public static final CustomLevel TRACE = CustomLevel.of("TRACE", 300, WHITE);
    public static final CustomLevel FINEST = CustomLevel.of("FINEST", 300, WHITE);

    private final String levelName;
    private final int priority;
    private final String levelFormat;

    protected CustomLevel(@NonNull String level, int priority) {
        super(level.toUpperCase(), priority);
        this.levelName = this.toString();
        this.priority = priority;
        this.levelFormat = RESET.getFormatString();
    }

    protected CustomLevel(@NonNull String level, int priority, @NonNull final String levelFormat) {
        super(level.toUpperCase(), priority);
        this.levelName = this.toString();
        this.priority = priority;
        this.levelFormat = levelFormat;
    }

    protected CustomLevel(@NonNull String level, int priority, @NonNull final LogLevelFormat format) {
        this(level, priority, format.getFormatString());
    }

    public static Optional<CustomLevel> convertJulEquivalent(@NonNull final Level level) {
        String levelName = level.getName().toUpperCase();
        if (ALL_LEVELS.containsKey(levelName)) {
            return Optional.of(ALL_LEVELS.get(levelName));
        }
        return Optional.empty();
    }

    public static Optional<CustomLevel> checkForCustomLevel(@NonNull String level) {
        if (level.isBlank()) {
            return Optional.empty();
        }

        level = level.toUpperCase();
        if (ALL_LEVELS.containsKey(level)) {
            return Optional.of(ALL_LEVELS.get(level));
        }

        try {
            int potentialPriority = Integer.parseInt(level);
            for (CustomLevel customLevel : ALL_LEVELS.values()) {
                if (customLevel.getPriority() == potentialPriority) {
                    return Optional.of(customLevel);
                }
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    public static CustomLevel of(@NonNull String level, final int priority) {
        return CustomLevel.parse(level, priority, RESET.getFormatString());
    }

    public static CustomLevel of(@NonNull String level, final int priority, @NonNull final String levelFormat) {
        return CustomLevel.parse(level, priority, levelFormat);
    }

    public static CustomLevel of(@NonNull String level, final int priority, @NonNull final LogLevelFormat format) {
        return CustomLevel.parse(level, priority, format.getFormatString());
    }

    public static CustomLevel parse(String name) throws IllegalArgumentException {
        Optional<CustomLevel> maybeLevel = checkForCustomLevel(name);
        if(maybeLevel.isPresent()) {
            return maybeLevel.get();
        }
        throw new IllegalArgumentException("bad level: " + name + ". Could not find a matching valid CustomLevel.");
    }

    private static CustomLevel parse(String levelNameToParse, final int priority, final String formatString) {
        levelNameToParse = levelNameToParse.toUpperCase();

        // Check if we already have this level
        CustomLevel ret = ALL_LEVELS.get(levelNameToParse);

        if (ret == null) {
            ret = new CustomLevel(levelNameToParse, priority, formatString);
            ALL_LEVELS.put(ret.getLevelName(), ret);
        } else if (ret.getPriority() != priority || !ret.getLevelFormat().equals(formatString)) {
            throw new IllegalArgumentException("A CustomLevel already exists with that name, but with a different priority or format.");
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
