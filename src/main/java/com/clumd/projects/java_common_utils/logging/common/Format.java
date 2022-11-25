package com.clumd.projects.java_common_utils.logging.common;

import com.clumd.projects.java_common_utils.logging.api.LogLevelFormat;
import lombok.Getter;

import java.util.Collection;

public enum Format implements LogLevelFormat {

    // MODIFIERS
    RESET("\033[0m"),
    BOLD("\033[1m"),
    UNDERLINE("\033[4m"),
    ITALIC("\033[3m"),
    OUTLINE("\033[51m"),

    // BASE COLOURS
    BLACK("\033[30m"),
    RED("\033[31m"),
    GREEN("\033[32m"),
    YELLOW("\033[33m"),
    BLUE("\033[34m"),
    PURPLE("\033[35m"),
    CYAN("\033[36m"),
    WHITE("\033[37m"),

    // BRIGHT COLOURS
    BRIGHT_BLACK("\033[90m"),
    BRIGHT_RED("\033[91m"),
    BRIGHT_GREEN("\033[92m"),
    BRIGHT_YELLOW("\033[93m"),
    BRIGHT_BLUE("\033[94m"),
    BRIGHT_PURPLE("\033[95m"),
    BRIGHT_CYAN("\033[96m"),
    BRIGHT_WHITE("\033[97m"),

    // BASE BACKGROUNDS
    BLACK_BACKGROUND("\033[40m"),
    RED_BACKGROUND("\033[41m"),
    GREEN_BACKGROUND("\033[42m"),
    YELLOW_BACKGROUND("\033[43m"),
    BLUE_BACKGROUND("\033[44m"),
    PURPLE_BACKGROUND("\033[45m"),
    CYAN_BACKGROUND("\033[46m"),
    WHITE_BACKGROUND("\033[47m"),

    // BRIGHT BACKGROUNDS
    BRIGHT_BLACK_BACKGROUND("\033[100m"),
    BRIGHT_RED_BACKGROUND("\033[101m"),
    BRIGHT_GREEN_BACKGROUND("\033[102m"),
    BRIGHT_YELLOW_BACKGROUND("\033[103m"),
    BRIGHT_BLUE_BACKGROUND("\033[104m"),
    BRIGHT_PURPLE_BACKGROUND("\033[105m"),
    BRIGHT_CYAN_BACKGROUND("\033[106m"),
    BRIGHT_WHITE_BACKGROUND("\033[107m");

    @Getter
    private final String formatString;

    Format(final String formatString) {
        this.formatString = formatString;
    }

    public static String createFormat(Collection<Format> formatModifiers) {
        formatModifiers = formatModifiers
                .stream()
                .filter(f -> f != RESET)
                .toList();

        StringBuilder modifiers = new StringBuilder("\033[0;");
        String formatString;

        for (Format f : formatModifiers) {
            modifiers.append(';');
            formatString = f.getFormatString();
            formatString = formatString.substring(
                    formatString.indexOf('[') + 1,
                    formatString.length() - 1
            );
            modifiers.append(formatString);
        }

        return modifiers.append('m').toString();
    }
}
