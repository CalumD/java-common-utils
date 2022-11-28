package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.files.FileUtils;
import com.clumd.projects.java_common_utils.logging.api.CustomLogController;
import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.controllers.ConsoleController;
import com.clumd.projects.java_common_utils.logging.controllers.FileController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public final class LogRoot {

    private static final Map<Long, String> OVERRIDDEN_THREAD_NAME_MAPPINGS = new HashMap<>();
    private static final UUID SPECIFIC_RUN_ID = UUID.randomUUID(); // The ID for a specific run, of a specific machine.
    private static final int SINGLE_FILE_LOG_SIZE = 10000000; //~10MB in bytes.
    private static final int LOG_FILE_ROTATIONS = 3; // max files to keep track of before re-writing old logs.
    public static final String TAB = "    ";
    public static final String ANON_THREAD = "Anon/Unknown Thread";

    private static String discardablePackageId;
    private static String loggingRootId;
    @Getter(value = AccessLevel.PACKAGE)
    private static String staticSystemName;

    private LogRoot() {
        // Don't allow this class to be instantiated. It should be used for static method calls only.
    }

    public static LogRoot init(
            @NonNull final String discardablePackageIdEndingInDot,
            @NonNull final String loggingRootID,
            final String systemID
    ) {
        LogRoot.discardablePackageId = discardablePackageIdEndingInDot;
        LogRoot.loggingRootId = loggingRootID;
        // Obtain the local system's name to identify its logfile in a distributed system.
        try {
            staticSystemName = systemID == null
                    ? InetAddress
                    .getLocalHost()
                    .toString()
                    .replace("/", "-")
                    .replace("\\\\", "-")
                    : systemID;
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Failed to obtain the local system's name.", e);
        }

        // Remove all parent chaining
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        root = Logger.getLogger(loggingRootId);
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        root.setLevel(CustomLevel.ALL);

        // Return an instance for self reference to the 'withHandlers' method for nice constructor fluency.
        // This method also ensures any default CustomLogControllers used have initialised static variables to reference
        return new LogRoot();
    }

    public static LogRoot init(
            @NonNull final String discardablePackageIdEndingInDot,
            @NonNull final String loggingRootID
    ) {
        return init(discardablePackageIdEndingInDot, loggingRootID, null);
    }

    public void withHandlers(Collection<CustomLogController> wantedLogHandlers) {

        Logger root = Logger.getLogger("");

        // init each wanted handler
        for (CustomLogController handler : wantedLogHandlers) {
            handler.acceptLogRootRefs(SPECIFIC_RUN_ID, staticSystemName, OVERRIDDEN_THREAD_NAME_MAPPINGS);
            if (handler instanceof StreamHandler streamHandler) {
                root.addHandler(streamHandler);
            } else {
                throw new IllegalArgumentException("Every custom log controller MUST extend java.util.logging.StreamHandler.");
            }
        }
    }

    public static CustomLogController basicConsoleHandler(boolean useSpacerLines) {
        return new ConsoleController(useSpacerLines);
    }

    public static CustomLogController basicFileHandler(@NonNull String atDir) throws IOException {
        FileUtils.makeAllDirs(atDir);
        return new FileController(
                atDir + "/" + loggingRootId + "_" + staticSystemName + "_%g.log",
                SINGLE_FILE_LOG_SIZE,
                LOG_FILE_ROTATIONS,
                true
        );
    }

    public static CustomLogController basicFileHandler() throws IOException {
        return basicFileHandler(
                new File(
                        LogRoot
                                .class
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .getFile()
                ).getAbsolutePath()
        );
    }

    public static ExtendedLogger createLogger(@NonNull final Class<?> forClass) {
        return createLogger(null, forClass.getName());
    }

    public static ExtendedLogger createLogger(@NonNull String loggerIdentifier) {
        return createLogger(null, loggerIdentifier);
    }

    private static String buildLogName(final String prefix, final String loggerIdentifier) {
        if (loggingRootId == null) {
            throw new ExceptionInInitializerError("Logging Root ID is not set, have you called the \"LogRoot.init\" method yet?");
        }

        return loggingRootId
                + '.' + (prefix == null ? "" : prefix + ":")
                + (
                loggerIdentifier.startsWith(discardablePackageId)
                        ? loggerIdentifier.substring(discardablePackageId.length())
                        : loggerIdentifier
        );
    }

    public static ExtendedLogger createLogger(final String prefix, final String loggerIdentifier) {
        String loggerName = buildLogName(prefix, loggerIdentifier);
        Logger extLog = LogManager.getLogManager().getLogger(loggerName);
        if (extLog == null) {
            extLog = new ExtendedLogger(loggerName);
            LogManager.getLogManager().addLogger(extLog);
        }
        return (ExtendedLogger) extLog;
    }

    public static void setBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, @NonNull final Logger viaLogger) {
        setGivenLoggersToLevel(
                getAllLoggerNames(viaLogger.getName()),
                selectedLevel
        );
    }

    public static void setBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, @NonNull final String viaLogIdentifier) {
        String loggerName = buildLogName(null, viaLogIdentifier);
        setGivenLoggersToLevel(
                getAllLoggerNames(loggerName),
                selectedLevel
        );
    }

    public static void setGlobalBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, @NonNull final String viaLogIdentifier) {
        setGivenLoggersToLevel(
                getAllLoggerNames(viaLogIdentifier),
                selectedLevel
        );
    }

    public static void setBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, final String viaLogPrefix, @NonNull final String viaLogIdentifier) {
        String loggerName = buildLogName(viaLogPrefix, viaLogIdentifier);
        setGivenLoggersToLevel(
                getAllLoggerNames(loggerName),
                selectedLevel
        );
    }

    public static void setGlobalLoggingLevel(@NonNull final CustomLevel selectedLevel) {
        setGivenLoggersToLevel(
                getAllLoggerNames(null),
                selectedLevel
        );
    }

    public static void setApplicationGlobalLevel(@NonNull final CustomLevel selectedLevel) {
        setGivenLoggersToLevel(
                getAllLoggerNames(loggingRootId),
                selectedLevel
        );
    }

    private static List<String> getAllLoggerNames(String filteredBy) {
        List<String> names =  Collections.list(
                LogManager
                        .getLogManager()
                        .getLoggerNames()
        );
        if (filteredBy != null) {
            return names
                    .stream()
                    .filter(n -> n.startsWith(filteredBy))
                    .toList();
        }
        return names;
    }

    private static void setGivenLoggersToLevel(final Collection<String> givenLoggers, final CustomLevel selectedLevel) {
        givenLoggers
                .forEach(logName -> Logger
                        .getLogger(logName)
                        .setLevel(selectedLevel)
                );
    }
}
