package com.clumd.projects.java_common_utils.logging.controllers;

import com.clumd.projects.java_common_utils.logging.api.CustomLogHandler;
import com.clumd.projects.java_common_utils.logging.api.LoggableData;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.api.Json;
import lombok.NonNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DenseFileController extends FileHandler implements CustomLogHandler {

    public final SimpleDateFormat fileDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Pass-through constructor ensuring we will use the desired custom formatter, and match ALL records.
     *
     * @param pathToLogFile     The pattern to match for the logfile's title.
     * @param singleFileLogSize This is the max file size before the logger will rotate files (in regular Bytes).
     * @param logFileRotations  This is the max number of log files to keep in rotation before overwriting the first
     *                          one.
     * @param appendMode        Should always be true to ensure we are in append mode.
     * @throws IOException       Thrown if we cannot find the location or there is an error getting it.
     * @throws SecurityException Thrown if we do not have the correct permissions to be writing to this location.
     */
    public DenseFileController(
            String pathToLogFile,
            int singleFileLogSize,
            int logFileRotations,
            boolean appendMode
    ) throws IOException, SecurityException {
        super(pathToLogFile, singleFileLogSize, logFileRotations, appendMode);
        this.setFormatter(new FileFormat());
        this.setLevel(Level.ALL);
    }

    @Override
    public void acceptLogRootRefs(@NonNull UUID specificRunID, @NonNull String systemID, @NonNull Map<Long, String> overriddenThreadNames) {
        // Unused params as this console logger is for basic facts only
    }

    /**
     * Used to format all text going to the logfile into a sensible form/layout.
     * <p>
     * An attempt is made to squash every entry into a json object for easier consumption/parsing down the line.
     */
    private final class FileFormat extends Formatter {

        /**
         * Used to remove all newlines and awkward quotes so that each line in the output file is a valid JSON object
         * for further processing outside this program. It is not GUARANTEED that the line will be valid JSON, though
         * best effort is being made.
         *
         * @param input This is the string to remove newlines and awkward quotes from.
         * @return This is the sanitised string that can be used in the logfile.
         */
        private String strFormatter(String input) {
            return input == null
                    ? "NULL"
                    : input
                    .replace("\n", "  ");           //rid newlines
        }

        @Override
        public String format(LogRecord logRecord) {
            StringBuilder ret = new StringBuilder();

            ret.append('[');
            ret.append(fileDateTimeFormatter.format(logRecord.getMillis()));

            ret.append(", ");
            ret.append(logRecord.getLevel().getName());

            if (logRecord instanceof ExtendedLogRecord elr) {
                if (elr.getBakedInTags() != null) {
                    ret.append(", ").append(elr.getBakedInTags());
                }
                if (elr.getTags() != null) {
                    ret.append(", ").append(elr.getTags());
                }
            }


            ret.append("]  ");
            ret.append(strFormatter(logRecord.getMessage()));
            ret.append('\n');

            // Check for a thrown error
            if (logRecord.getThrown() != null) {
                boolean isTopReason = true;
                Throwable throwable = logRecord.getThrown();
                do {
                    if (isTopReason) {
                        ret
                                .append(strFormatter("Error:  (" + throwable.getClass().getSimpleName() + ") " + throwable.getMessage()))
                                .append('\n');
                        isTopReason = false;
                    } else {
                        ret
                                .append(strFormatter("Nested Reason:  (" + throwable.getClass().getSimpleName() + ") " + throwable.getMessage()))
                                .append('\n');
                    }
                    for (Object stackTraceLine : throwable.getStackTrace()) {
                        ret
                                .append(strFormatter("  " + stackTraceLine.toString()))
                                .append('\n');
                    }
                    throwable = throwable.getCause();
                } while (throwable != null && throwable != throwable.getCause());
            }

            // Check for additional metadata about the log entry.
            if (logRecord.getParameters() != null) {
                ret.append('[');
                for (Object metadata : logRecord.getParameters()) {
                    switch (metadata) {
                        case LoggableData loggableMetadata ->
                                ret.append(strFormatter(loggableMetadata.getFormattedLogData()));
                        case Json jsonMetadata ->
                                ret.append(jsonMetadata.asString(2));
                        case null ->
                                ret.append("NULL");
                        default ->
                                ret.append(strFormatter(metadata.toString()));
                    }
                    ret.append(", ");
                }
                ret.delete(ret.length() - 2, ret.length());
                ret.append("]\n");
            }

            return ret.toString();
        }
    }
}
