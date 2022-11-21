package com.clumd.projects.java_common_utils.logging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public final class LogRoot {

    private static final Map<Long, String> OVERRIDDEN_THREAD_NAME_MAPPINGS = new HashMap<>();
    private static final UUID SPECIFIC_RUN_ID = UUID.randomUUID(); // The ID for a specific run, of a specific machine.
    private static final int SINGLE_FILE_LOG_SIZE = 10000000; //~10MB in bytes.
    private static final int LOG_FILE_ROTATIONS = 3; // max files to keep track of before re-writing old logs.
    static final String TAB = "    ";
    static final String ANON_THREAD = "Anon/Unknown Thread";

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
        // Replace the default LogManager so that when the shutdown hook halts all running threads, the logger
        // is able to run until the very end, when the end of our custom Shutdown hook should kill it.
        System.setProperty("java.util.logging.manager", WaitForShutdownHookLogManager.class.getName());
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
        root.setUseParentHandlers(false);
        root.setLevel(CustomLevel.ALL);

        // Return an instance for self reference to the 'withHandlers' method for nice constructor fluency.
        // This method also ensures any default CustomLogControllers used have initialised static variables to reference
        return new LogRoot();
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

    public static CustomLogController basicConsoleHandler() {
        return new ConsoleController();
    }

    public static CustomLogController basicFileHandler(@NonNull String atDir) throws IOException {
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

    public static Logger createLogger(@NonNull final Class<?> forClass) {
        return createLogger(null, forClass.getName());
    }

    public static Logger createLogger(final String prefix, final String loggerIdentifier) {
        if (loggingRootId == null) {
            throw new ExceptionInInitializerError("Logging Root ID is not set, have you called the \"LogRoot.init\" method yet?");
        }

        //create the logger object
        return Logger.getLogger(loggingRootId
                        + '.' + (prefix == null ? "" : prefix)
                        + (
                        loggerIdentifier.startsWith(discardablePackageId)
                                ? loggerIdentifier.substring(discardablePackageId.length())
                                : loggerIdentifier
                )
        );
    }

    public static void setBranchLoggingLevel(@NonNull final LogLevel selectedLevel, final String viaLogPrefix, @NonNull final String viaLogIdentifier) {
        // TODO
    }


    public static void setGlobalLoggingLevel(@NonNull final LogLevel selectedLevel) {
        Logger.getLogger("").setLevel((Level) selectedLevel);
    }

    /**
     * Simple static subclass to maintain a permanent reference to the root instance of the Log manager, and override
     * the default behaviour of reset() to allow us to only call reset once we are ready.
     */
    public static class WaitForShutdownHookLogManager extends LogManager {

        static WaitForShutdownHookLogManager instance;

        public WaitForShutdownHookLogManager() {
            instance = this;
        }

        public static void finalReset() {
            instance.reset0();
        }

        @Override
        public void reset() { /* wait for custom reset to be called. */ }

        private void reset0() {
            super.reset();
        }
    }
}
