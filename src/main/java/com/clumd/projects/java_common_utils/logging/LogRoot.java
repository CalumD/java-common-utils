package com.clumd.projects.java_common_utils.logging;

import lombok.NonNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class LogRoot {

    private static final UUID SPECIFIC_RUN_ID = UUID.randomUUID(); // The ID for a specific run, of a specific machine.
    private static final Map<Long, String> OVERRIDDEN_THREAD_NAME_MAPPINGS = new HashMap<>();

    private static String discardablePackageId;
    private static String loggingRootId;
    private static String staticSystemName;

    private LogRoot() {
        // Don't allow this class to be instantiated. It should be used for static method calls only.
    }

    public static void init(
            @NonNull final String discardablePackageIdEndingInDot,
            @NonNull final String loggingRootID,
            final String systemID,
            final Collection<CustomLogController> wantedLogHandlers
    ) {
        // Replace the default LogManager so that when the shutdown hook halts all running threads, the logger
        // is able to run until the very end, when the end of our custom Shutdown hook should kill it.
        System.setProperty("java.util.logging.manager", WaitForShutdownHookLogManager.class.getName());
        LogRoot.discardablePackageId = discardablePackageIdEndingInDot;
        LogRoot.loggingRootId = loggingRootID;
        // Obtain the local system's name to identify its logfile in a distributed system.
        try {
            staticSystemName = InetAddress
                    .getLocalHost()
                    .toString()
                    .replace("/", "-")
                    .replace("\\\\", "-");
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

        // init each wanted handler
        for (CustomLogController handler : wantedLogHandlers) {
            handler.acceptLogRootRefs(SPECIFIC_RUN_ID, systemID == null ? staticSystemName : systemID, OVERRIDDEN_THREAD_NAME_MAPPINGS);
            if (handler instanceof StreamHandler streamHandler) {
                root.addHandler(streamHandler);
            } else {
                throw new IllegalArgumentException("Every custom log controller MUST extend java.util.logging.StreamHandler.");
            }
        }
    }

    public static ConsoleHandler basicConsoleHandler() {
        return new ConsoleHandler();
    }

    public static FileHandler basicFileHandler() throws IOException {
        // TODO: STILL TO SET THE DEFAULT PARAMS
        return new FileHandler("", true);
    }

    public static Logger createLogger(@NonNull final Class<?> forClass) {
        return createLogger(null, forClass.getName());
    }

    public static Logger createLogger(final String prefix, final String loggerIdentifier) {
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
