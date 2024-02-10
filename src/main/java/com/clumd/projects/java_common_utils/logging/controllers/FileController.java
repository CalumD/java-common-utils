package com.clumd.projects.java_common_utils.logging.controllers;

import com.clumd.projects.java_common_utils.logging.api.CustomLogHandler;
import com.clumd.projects.java_common_utils.logging.api.LoggableData;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.api.Json;
import com.clumd.projects.javajson.api.JsonBuilder;
import com.clumd.projects.javajson.core.BasicJsonBuilder;
import lombok.NonNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import static com.clumd.projects.java_common_utils.logging.LogRoot.ANON_THREAD;

public class FileController extends FileHandler implements CustomLogHandler {

    private UUID traceID;
    private String systemID;
    private Map<Long, String> overriddenThreadNames;
    public final SimpleDateFormat fileDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Pass-through constructor ensuring we will use the desired custom formatter, and match ALL records.
     *
     * @param pathToLogFile     The pattern to match for the logfile's title.
     * @param singleFileLogSize This is the max file size before the logger will rotate files (in regular Bytes).
     * @param logFileRotations  This is the max number of log files to keep in rotation before overwriting the first
     *                          one.
     * @param appendMode        Should always be true to ensure we are in append mode.
     * @throws IOException Thrown if we cannot find the location or there is an error getting it.
     * @throws SecurityException Thrown if we do not have the correct permissions to be writing to this location.
     */
    public FileController(
            String pathToLogFile,
            int singleFileLogSize,
            int logFileRotations,
            boolean appendMode
    ) throws IOException, SecurityException {
        super(pathToLogFile, singleFileLogSize, logFileRotations, appendMode);
        super.setFormatter(new FileFormat());
        this.setLevel(Level.ALL);
    }

    @Override
    public void acceptLogRootRefs(@NonNull UUID specificRunID, @NonNull String systemID, @NonNull Map<Long, String> overriddenThreadNames) {
        this.traceID = specificRunID;
        this.systemID = systemID;
        this.overriddenThreadNames = overriddenThreadNames;
    }

    @Override
    public boolean isLoggable(LogRecord logRecord) {
        if (logRecord instanceof ExtendedLogRecord elr && elr.getControllersWhichShouldDisregardThisMessage() != null) {
            for (Class<? extends StreamHandler> controllerWhichShouldDisregard : elr.getControllersWhichShouldDisregardThisMessage()) {
                if (controllerWhichShouldDisregard.isAssignableFrom(this.getClass())) {
                    return false;
                }
            }
        }
        return super.isLoggable(logRecord);
    }

    @Override
    public void setFormatter(Formatter newFormatter) throws SecurityException {
        // EXPLICIT DENY OTHER THINGS SETTING OUR FORMATTER - GOSH DARN IT SPRING / EMBEDDED TOMCAT
        if (getFormatter() != null) {
            System.err.println("WARNING: Attempt to reset " + FileController.class.getName() + " formatter.");
        }
    }

    /**
     * Used to format all text going to the logfile into a sensible form/layout.
     * <p>
     * An attempt is made to squash every entry into a json object for easier consumption/parsing down the line.
     */
    private final class FileFormat extends Formatter {

        private static final String EXCEPTION_ARRAY = "error[]";
        private static final String METADATA_ARRAY = "meta[]";

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
                    .replace("\n", "  ")         //rid newlines
                    .replace("\\\"", "\\\\\"")   //escape, escaped quotes
                    .replace("\"", "\\\"");      //escape quotes
        }

        @Override
        public String format(LogRecord logRecord) {
            JsonBuilder logEntry = new BasicJsonBuilder();

            // Add all the basic info
            logEntry.addString("publisher", systemID)
                    .addString("traceID", traceID.toString())
                    .addString("dateTime", fileDateTimeFormatter.format(logRecord.getMillis()))
                    .addLong("machineDateTime", logRecord.getMillis())
                    .addString("logger", Objects.requireNonNullElse(logRecord.getLoggerName(), "Anon/Unknown Logger"))
                    .addLong("threadID", logRecord.getLongThreadID())
                    .addString("threadName", Objects.requireNonNullElse(overriddenThreadNames.get(logRecord.getLongThreadID()), ANON_THREAD))
                    .addString("level", logRecord.getLevel().getName())
                    .addString("message", strFormatter(logRecord.getMessage()));

            // Check if we have tags to write
            if (logRecord instanceof ExtendedLogRecord elr) {
                if (elr.getBakedInTags() != null) {
                    elr.getBakedInTags().forEach(t -> logEntry.addString("tags[]", t));
                }
                if (elr.getTags() != null) {
                    elr.getTags().forEach(t -> logEntry.addString("tags[]", t));
                }
            }

            // Check for a thrown error
            if (logRecord.getThrown() != null) {
                boolean isTopReason = true;
                Throwable throwable = logRecord.getThrown();
                do {
                    if (isTopReason) {
                        logEntry.addString(
                                EXCEPTION_ARRAY,
                                strFormatter("Error:  (" + throwable.getClass().getSimpleName() + ") " + throwable.getMessage())
                        );
                        isTopReason = false;
                    } else {
                        logEntry.addString(
                                EXCEPTION_ARRAY,
                                strFormatter("Nested Reason:  (" + throwable.getClass().getSimpleName() + ") " + throwable.getMessage())
                        );
                    }
                    for (Object stackTraceLine : throwable.getStackTrace()) {
                        logEntry.addString(EXCEPTION_ARRAY, strFormatter("  " + stackTraceLine.toString()));
                    }
                    throwable = throwable.getCause();
                } while (throwable != null && throwable != throwable.getCause());
            }

            // Check for additional metadata about the log entry.
            if (logRecord.getParameters() != null) {
                for (Object metadata : logRecord.getParameters()) {
                    switch (metadata) {
                        case LoggableData loggableMetadata ->
                                logEntry.addString(METADATA_ARRAY, strFormatter(loggableMetadata.getFormattedLogData()));
                        case Json jsonMetadata ->
                                logEntry.addBuilderBlock(METADATA_ARRAY, jsonMetadata);
                        case null ->
                                logEntry.addString(METADATA_ARRAY, "NULL");
                        default ->
                                logEntry.addString(METADATA_ARRAY, strFormatter(metadata.toString()));
                    }
                }
            }

            return logEntry.build().asString() + "\n";
        }
    }
}
