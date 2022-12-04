package com.clumd.projects.java_common_utils.logging.controllers;

import com.clumd.projects.java_common_utils.logging.api.CustomLogHandler;
import com.clumd.projects.java_common_utils.logging.api.LogLevel;
import com.clumd.projects.java_common_utils.logging.api.LoggableData;
import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import com.clumd.projects.java_common_utils.logging.common.Format;
import com.clumd.projects.javajson.api.Json;
import lombok.NonNull;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import static com.clumd.projects.java_common_utils.logging.LogRoot.ANON_THREAD;
import static com.clumd.projects.java_common_utils.logging.LogRoot.TAB;

public class ConsoleController extends ConsoleHandler implements CustomLogHandler {

    private UUID traceID;
    private String systemID;
    private Map<Long, String> overriddenThreadNames;
    public static final SimpleDateFormat CONSOLE_DATE_TIME_FORMATTER = new SimpleDateFormat("EEE dd/MMM/yyyy HH:mm:ss.SSS");
    private final boolean useSpacerLine;

    public ConsoleController(boolean useSpacerLines) {
        super();
        this.setFormatter(new ConsoleFormat());
        this.setLevel(CustomLevel.ALL);
        this.useSpacerLine = useSpacerLines;
    }

    @Override
    public void acceptLogRootRefs(@NonNull UUID specificRunID, @NonNull String systemID, @NonNull Map<Long, String> overriddenThreadNames) {
        this.traceID = specificRunID;
        this.systemID = systemID;
        this.overriddenThreadNames = overriddenThreadNames;
    }

    private final class ConsoleFormat extends Formatter {

        @Override
        public String format(LogRecord logRecord) {
            if (logRecord.getLevel() instanceof LogLevel logLevel) {
                return formatWithColour(logRecord, logLevel);
            } else {
                return formatWithoutColour(logRecord);
            }
        }

        private String formatWithoutColour(LogRecord logRecord) {
            StringBuilder ret = new StringBuilder();

            // Provide meta data info
            formatMetadata(ret, logRecord);

            // Give the main message to log
            ret
                    .append("Message<").append(logRecord.getLevel().getName()).append(">:  ")
                    .append(logRecord.getMessage())
                    .append("\n");

            // Check if the log contains an error
            return formatThrowablesAndData(ret, logRecord);
        }

        private String formatWithColour(LogRecord logRecord, LogLevel level) {
            StringBuilder ret = new StringBuilder();

            // Apply colour
            ret.append(level.getLevelFormat());

            // Provide meta data info
            formatMetadata(ret, logRecord);

            // Normalise Colours and give the main message to log
            ret
                    .append("Message<").append(level.getLevelName()).append(">:  ")
                    .append(Format.RESET.getFormatString())
                    .append(logRecord.getMessage())
                    .append("\n");

            // Check if the log contains an error
            return formatThrowablesAndData(ret, logRecord);
        }

        private void formatMetadata(StringBuilder ret, LogRecord logRecord) {
            ret.append(traceID).append(TAB)
                    .append(systemID).append(TAB)
                    .append(CONSOLE_DATE_TIME_FORMATTER.format(logRecord.getMillis())).append(TAB)
                    .append(logRecord.getLoggerName()).append(TAB)
                    .append('(').append(logRecord.getLongThreadID()).append("):")
                    .append(Objects.requireNonNullElse(overriddenThreadNames.get(logRecord.getLongThreadID()), ANON_THREAD)).append(TAB)
                    .append("\n");
            if (logRecord instanceof ExtendedLogRecord elr && elr.getTags() != null) {
                ret.append(elr.getTags()).append("\n");
            }
        }

        private String formatThrowablesAndData(StringBuilder ret, LogRecord logRecord) {
            if (logRecord.getThrown() != null) {
                boolean isTopReason = true;
                Throwable throwable = logRecord.getThrown();
                do {
                    if (isTopReason) {
                        ret.append("Error:  ");
                        isTopReason = false;
                    } else {
                        ret.append("Nested Reason:  ");
                    }
                    ret.append("(").append(throwable.getClass().getSimpleName()).append(") ");
                    ret.append(throwable.getMessage()).append("\n");
                    for (Object stackTraceLine : throwable.getStackTrace()) {
                        ret.append("  ").append(stackTraceLine.toString()).append("\n");
                    }
                    throwable = throwable.getCause();
                } while (throwable != null && throwable != throwable.getCause());
            }

            //check for additional metadata about the log entry.
            if (logRecord.getParameters() != null && logRecord.getParameters().length > 0) {
                ret.append("Metadata:  <").append(logRecord.getParameters().length).append("> item(s)\n");
                for (Object item : logRecord.getParameters()) {
                    if (item instanceof LoggableData loggableData) {
                        ret.append("{\n").append(loggableData.getFormattedLogData()).append("\n}\n");
                    } else if (item instanceof Json jsonItem) {
                        ret.append((jsonItem).asPrettyString(2)).append(logRecord.getParameters().length > 1 ? '\n' : "");
                    } else if (item == null) {
                        ret.append("{ ").append("NULL").append(" }\n");
                    } else {
                        ret.append("{ ").append(item.toString()).append(" }\n");
                    }
                }
            }
            if (useSpacerLine) {
                ret.append("\n");
            }
            return ret.toString();
        }
    }
}
