package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.javajson.api.Json;
import lombok.NonNull;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleController extends ConsoleHandler implements CustomLogController {

    private UUID traceID;
    private String systemID;
    private Map<Long, String> overriddenThreadNames;
    static final SimpleDateFormat CONSOLE_DATE_TIME_FORMATTER = new SimpleDateFormat("EEE dd/MMM/yyyy HH:mm:ss.SSS");

    public ConsoleController() {
        super();
        this.setFormatter(new ConsoleFormat());
        this.setLevel(CustomLevel.ALL);
    }

    @Override
    public void acceptLogRootRefs(@NonNull UUID specificRunID, @NonNull String systemID, @NonNull Map<Long, String> overriddenThreadNames) {
        this.traceID = specificRunID;
        this.systemID = systemID;
        this.overriddenThreadNames = overriddenThreadNames;
    }

    private class ConsoleFormat extends Formatter {

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
            ret.append(traceID).append("    ")
                    .append(systemID).append("    ")
                    .append(CONSOLE_DATE_TIME_FORMATTER.format(logRecord.getMillis())).append("    ")
                    .append(logRecord.getLoggerName()).append("    ")
                    .append('(').append(logRecord.getLongThreadID()).append("):")
                    .append(Objects.requireNonNullElse(overriddenThreadNames.get(logRecord.getLongThreadID()), "Anon/Unknown Thread"))
                    .append("    \n");
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
                ret.append("\n");
            }

            //check for additional metadata about the log entry.
            if (logRecord.getParameters() != null && logRecord.getParameters().length > 0) {
                ret.append("Metadata:  <").append(logRecord.getParameters().length).append("> item(s)\n");
                for (Object item : logRecord.getParameters()) {
                    if (item instanceof Json jsonItem) {
                        ret.append((jsonItem).asPrettyString(2)).append(logRecord.getParameters().length > 1 ? '\n' : "");
                    } else {
                        ret.append("{\n").append(item.toString()).append("\n}\n");
                    }
                }
            }
            return ret.append("\n").toString();
        }
    }
}
