package com.clumd.projects.java_common_utils.logging;

import lombok.Getter;

public enum LevelFormat {

    RESET("\033[0m"),
    BOLD("\033[0;1m"),

    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m"),

    BRIGHT_BLACK("\033[0;90m"),
    BRIGHT_RED("\033[0;91m"),
    BRIGHT_GREEN("\033[0;92m"),
    BRIGHT_YELLOW("\033[0;93m"),
    BRIGHT_BLUE("\033[0;94m"),
    BRIGHT_PURPLE("\033[0;95m"),
    BRIGHT_CYAN("\033[0;96m"),
    BRIGHT_WHITE("\033[0;97m"),

    BLACK_BACKGROUND("\033[40m"),
    RED_BACKGROUND("\033[41m"),
    GREEN_BACKGROUND("\033[42m"),
    YELLOW_BACKGROUND("\033[43m"),
    BLUE_BACKGROUND("\033[44m"),
    PURPLE_BACKGROUND("\033[45m"),
    CYAN_BACKGROUND("\033[46m"),
    WHITE_BACKGROUND("\033[47m"),

    BRIGHT_BLACK_BACKGROUND("\033[0;100m"),
    BRIGHT_RED_BACKGROUND("\033[0;101m"),
    BRIGHT_GREEN_BACKGROUND("\033[0;102m"),
    BRIGHT_YELLOW_BACKGROUND("\033[0;103m"),
    BRIGHT_BLUE_BACKGROUND("\033[0;104m"),
    BRIGHT_PURPLE_BACKGROUND("\033[0;105m"),
    BRIGHT_CYAN_BACKGROUND("\033[0;106m"),
    BRIGHT_WHITE_BACKGROUND("\033[0;107m"),

    BLACK_UNDERLINE("\033[4;30m"),
    RED_UNDERLINE("\033[4;31m"),
    GREEN_UNDERLINE("\033[4;32m"),
    YELLOW_UNDERLINE("\033[4;33m"),
    BLUE_UNDERLINE("\033[4;34m"),
    PURPLE_UNDERLINE("\033[4;35m"),
    CYAN_UNDERLINE("\033[4;36m"),
    WHITE_UNDERLINE("\033[4;37m");

    @Getter
    private final String formatString;

    LevelFormat(final String formatString) {
        this.formatString = formatString;
    }
}
