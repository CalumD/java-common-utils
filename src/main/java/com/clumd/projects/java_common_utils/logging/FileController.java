package com.clumd.projects.java_common_utils.logging;

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

import static com.clumd.projects.java_common_utils.logging.LogRoot.ANON_THREAD;

public class FileController extends FileHandler implements CustomLogController {

    private UUID traceID;
    private String systemID;
    private Map<Long, String> overriddenThreadNames;
    public static final SimpleDateFormat FILE_DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

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
    FileController(
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
        this.traceID = specificRunID;
        this.systemID = systemID;
        this.overriddenThreadNames = overriddenThreadNames;
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
                    .replaceAll("\\n", "  ")            //rid newlines
                    .replaceAll("\\\\\"", "\\\\\\\"")   //escape, escaped quotes
                    .replaceAll("\"", "\\\\\"");        //escape quotes
        }

        @Override
        public String format(LogRecord logRecord) {
            JsonBuilder logEntry = new BasicJsonBuilder();

            // Add all the basic info
            logEntry.addString("publisher", systemID)
                    .addString("traceID", traceID.toString())
                    .addString("dateTime", FILE_DATE_TIME_FORMATTER.format(logRecord.getMillis()))
                    .addLong("machineDateTime", logRecord.getMillis())
                    .addString("logger", Objects.requireNonNullElse(logRecord.getLoggerName(), "Anon/Unknown Logger"))
                    .addLong("threadID", logRecord.getLongThreadID())
                    .addString("threadName", Objects.requireNonNullElse(overriddenThreadNames.get(logRecord.getLongThreadID()), ANON_THREAD))
                    .addString("level", logRecord.getLevel().getName())
                    .addString("message", strFormatter(logRecord.getMessage()));

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
            if (logRecord.getParameters() != null && logRecord.getParameters().length > 0) {
                for (Object metadata : logRecord.getParameters()) {
                    if (metadata instanceof Json jsonMetadata) {
                        logEntry.addBuilderBlock(METADATA_ARRAY, jsonMetadata);
                    } else {
                        logEntry.addString(METADATA_ARRAY, strFormatter(metadata.toString()));
                    }
                }
            }

            return logEntry.build().asString() + "\n";
        }
    }
}
