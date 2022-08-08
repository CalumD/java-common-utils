package com.clumd.projects.java_common_utils.logging;

import lombok.NonNull;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogRoot {

    private static final UUID SPECIFIC_RUN_ID = UUID.randomUUID(); // The ID for a specific run, of a specific machine.
    private static final Map<Integer, String> OVERRIDDEN_THREAD_NAME_MAPPINGS = new HashMap<>();

    private static String discardablePackageId;
    private static String loggingRootId;

    private LogRoot() {
        // Don't allow this class to be instantiated. It should be used for static method calls only.
    }

    public static void init(
            @NonNull final String discardablePackageIdEndingInDot,
            @NonNull final String loggingRootId,
            final Collection<CustomLogHandler> wantedLogHandlers
    ) {
        // Replace the default LogManager so that when the shutdown hook halts all running threads, the logger
        // is able to run until the very end, when the end of our custom Shutdown hook should kill it.
        System.setProperty("java.util.logging.manager", WaitForShutdownHookLogManager.class.getName());
        LogRoot.discardablePackageId = discardablePackageIdEndingInDot;
        LogRoot.loggingRootId = loggingRootId;

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

        // init each wanted handler
        for (CustomLogHandler handler : wantedLogHandlers) {
            handler.acceptLogRootRefs(SPECIFIC_RUN_ID, OVERRIDDEN_THREAD_NAME_MAPPINGS);
        }
    }

    public ConsoleHandler basicConsoleHandler() {
        // TODO
        return new ConsoleHandler();
    }

    public FileHandler basicFileHandler() throws IOException {
        // TODO
        return new FileHandler("", true);
    }


    public static void setBranchLoggingLevel(@NonNull final LogLevel selectedLevel, final String viaLogPrefix, @NonNull final String viaLogIdentifier) {
        // TODO
    }


    public static void setGlobalLoggingLevel(@NonNull final LogLevel selectedLevel) {
        // TODO
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
