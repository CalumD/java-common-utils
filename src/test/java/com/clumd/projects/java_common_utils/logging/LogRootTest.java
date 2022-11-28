package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.files.FileUtils;
import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.controllers.ConsoleController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class LogRootTest {

    private static final String LOGGING_TEST_PATH = "src/test/resources/logging/logRoot";
    private static final String DISCARDABLE_PACKAGE = "com.clumd.projects.java_common_utils.logging.";
    private static final String LOGGING_ROOT = "L_T_R";

    @Captor
    private ArgumentCaptor<LogRecord> logCaptor;

    private class ExtendedConsoleController extends ConsoleController {
        public ExtendedConsoleController(boolean useSpacerLines) {
            super(useSpacerLines);
        }
    }

    @Mock
    private ExtendedConsoleController mockController;

    @BeforeEach
    void setup() throws IOException {
        lenient().doNothing().when(mockController).publish(logCaptor.capture());
        FileUtils.deleteDirectoryIfExists(LOGGING_TEST_PATH);
        LogRoot.init(
                        DISCARDABLE_PACKAGE,
                        LOGGING_ROOT)
                .withHandlers(List.of(
                        mockController,
                        LogRoot.basicConsoleHandler(true)
                ));
    }

    @AfterEach
    void tearDown() {
        LogManager.getLogManager().reset();
    }

    @Test
    void checkNewLoggersArePrefixedWithLTR() {
        assertEquals("L_T_R.some.class.name", LogRoot.createLogger("some.class.name").getName());
    }

    @Test
    void checkLoggersRemoveComDotClumd() {
        assertEquals("L_T_R.myFirstName", LogRoot.createLogger("com.clumd.projects.java_common_utils.logging.myFirstName").getName());
    }

    @Test
    void checkLoggerCustomPrefixName() {
        assertEquals("L_T_R.customPrefix:actual.logger.identifier",
                LogRoot.createLogger("customPrefix", "actual.logger.identifier").getName());
    }

    @Test
    void checkCreatingWithClass() {
        assertDoesNotThrow(() -> LogRoot.createLogger(LogRootTest.class));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *  The following tests will use the following logger hierarchy.
     * <p/>
     *                 TOP
     *                  |
     *            - - - - - - -
     *            |     |     |
     *           1a     1b    1c
     *            |           |
     *           2a           2b
     *            |
     *      - - - - - - -
     *      |     |     |
     *     3a     3b    3c
     *            |
     *            4a
     */


    private Map<String, Logger> setupLoggers() {
        Map<String, Logger> loggers = new HashMap<>();

        ExtendedLogger top = LogRoot.createLogger("top");

        ExtendedLogger _1a = LogRoot.createLogger("top.1a");
        ExtendedLogger _1b = LogRoot.createLogger("top.1b");
        ExtendedLogger _1c = LogRoot.createLogger("top.1c");

        ExtendedLogger _2a = LogRoot.createLogger("top.1a.2a");
        ExtendedLogger _2b = LogRoot.createLogger("top.1c.2b");

        ExtendedLogger _3a = LogRoot.createLogger("top.1a.2a.3a");
        ExtendedLogger _3b = LogRoot.createLogger("top.1a.2a.3b");
        ExtendedLogger _3c = LogRoot.createLogger("top.1a.2a.3c");

        ExtendedLogger _4a = LogRoot.createLogger("top.1a.2a.3b.4a");


        loggers.put("top", top);
        loggers.put("1a", _1a);
        loggers.put("1b", _1b);
        loggers.put("1c", _1c);
        loggers.put("2a", _2a);
        loggers.put("2b", _2b);
        loggers.put("3a", _3a);
        loggers.put("3b", _3b);
        loggers.put("3c", _3c);
        loggers.put("4a", _4a);
        return loggers;
    }

    @Test
    void test_default_is_all() {
        Map<String, Logger> l = setupLoggers();

        l.get("top").log(CustomLevel.SHUTDOWN, "first");  // Testing highest level okay
        l.get("1a").log(CustomLevel.EMERGENCY, "first");
        l.get("1b").log(CustomLevel.FAILURE, "first");
        l.get("1c").log(CustomLevel.SEVERE, "first");
        l.get("2a").log(CustomLevel.WARNING, "first");
        l.get("2b").log(CustomLevel.IMPORTANT, "first");
        l.get("3a").log(CustomLevel.INFO, "first");
        l.get("3b").log(CustomLevel.CONFIG, "first");
        l.get("3c").log(CustomLevel.VERBOSE, "first");
        l.get("4a").log(CustomLevel.TRACE, "first");  // Testing lowest level okay


        List<LogRecord> capturedLogs = logCaptor.getAllValues();

        assertEquals(10, capturedLogs.size(), 0);
        assertEquals(LOGGING_ROOT + ".top", capturedLogs.get(0).getLoggerName());
        assertEquals("first", capturedLogs.get(0).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(1).getLoggerName());
        assertEquals("first", capturedLogs.get(1).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1b", capturedLogs.get(2).getLoggerName());
        assertEquals("first", capturedLogs.get(2).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(3).getLoggerName());
        assertEquals("first", capturedLogs.get(3).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a.2a", capturedLogs.get(4).getLoggerName());
        assertEquals("first", capturedLogs.get(4).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c.2b", capturedLogs.get(5).getLoggerName());
        assertEquals("first", capturedLogs.get(5).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a.2a.3a", capturedLogs.get(6).getLoggerName());
        assertEquals("first", capturedLogs.get(6).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a.2a.3b", capturedLogs.get(7).getLoggerName());
        assertEquals("first", capturedLogs.get(7).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a.2a.3c", capturedLogs.get(8).getLoggerName());
        assertEquals("first", capturedLogs.get(8).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a.2a.3b.4a", capturedLogs.get(9).getLoggerName());
        assertEquals("first", capturedLogs.get(9).getMessage());
    }

    @Test
    void test_global_INFO_filters_levels_below() {
        Map<String, Logger> l = setupLoggers();

        l.get("1a").log(CustomLevel.NOTIFICATION, "first higher");
        l.get("1a").log(CustomLevel.DATA, "first lower");

        l.get("1b").log(CustomLevel.NOTIFICATION, "first higher");
        l.get("1b").log(CustomLevel.DATA, "first lower");

        l.get("1c").log(CustomLevel.NOTIFICATION, "first higher");
        l.get("1c").log(CustomLevel.DATA, "first lower");


        LogRoot.setGlobalLoggingLevel(CustomLevel.INFO);


        l.get("1a").log(CustomLevel.NOTIFICATION, "second higher");
        l.get("1a").log(CustomLevel.DATA, "second lower");

        l.get("1b").log(CustomLevel.NOTIFICATION, "second higher");
        l.get("1b").log(CustomLevel.DATA, "second lower");

        l.get("1c").log(CustomLevel.NOTIFICATION, "second higher");
        l.get("1c").log(CustomLevel.DATA, "second lower");


        List<LogRecord> capturedLogs = logCaptor.getAllValues();
        assertEquals(9, capturedLogs.size(), 0);

        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(0).getLoggerName());
        assertEquals("first higher", capturedLogs.get(0).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(1).getLoggerName());
        assertEquals("first lower", capturedLogs.get(1).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1b", capturedLogs.get(2).getLoggerName());
        assertEquals("first higher", capturedLogs.get(2).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1b", capturedLogs.get(3).getLoggerName());
        assertEquals("first lower", capturedLogs.get(3).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(4).getLoggerName());
        assertEquals("first higher", capturedLogs.get(4).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(5).getLoggerName());
        assertEquals("first lower", capturedLogs.get(5).getMessage());

        //////////////////

        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(6).getLoggerName());
        assertEquals("second higher", capturedLogs.get(6).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1b", capturedLogs.get(7).getLoggerName());
        assertEquals("second higher", capturedLogs.get(7).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(8).getLoggerName());
        assertEquals("second higher", capturedLogs.get(8).getMessage());
    }

    @Test
    void test_branch_INFO_filters_only_on_branch() {
        Map<String, Logger> l = setupLoggers();

        l.get("1a").log(CustomLevel.NOTIFICATION, "first higher");
        l.get("1a").log(CustomLevel.DATA, "first lower");

        l.get("2b").log(CustomLevel.NOTIFICATION, "first higher");
        l.get("2b").log(CustomLevel.DATA, "first lower");

        l.get("3c").log(CustomLevel.NOTIFICATION, "first higher");
        l.get("3c").log(CustomLevel.DATA, "first lower");


        LogRoot.setBranchLoggingLevel(CustomLevel.INFO, l.get("2a"));


        l.get("1a").log(CustomLevel.NOTIFICATION, "second higher");
        l.get("1a").log(CustomLevel.DATA, "second lower");

        l.get("2b").log(CustomLevel.NOTIFICATION, "second higher");
        l.get("2b").log(CustomLevel.DATA, "second lower");

        l.get("3c").log(CustomLevel.NOTIFICATION, "second higher");
        l.get("3c").log(CustomLevel.DATA, "second lower");


        List<LogRecord> capturedLogs = logCaptor.getAllValues();
        assertEquals(11, capturedLogs.size(), 0);

        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(0).getLoggerName());
        assertEquals("first higher", capturedLogs.get(0).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(1).getLoggerName());
        assertEquals("first lower", capturedLogs.get(1).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1c.2b", capturedLogs.get(2).getLoggerName());
        assertEquals("first higher", capturedLogs.get(2).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c.2b", capturedLogs.get(3).getLoggerName());
        assertEquals("first lower", capturedLogs.get(3).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1a.2a.3c", capturedLogs.get(4).getLoggerName());
        assertEquals("first higher", capturedLogs.get(4).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a.2a.3c", capturedLogs.get(5).getLoggerName());
        assertEquals("first lower", capturedLogs.get(5).getMessage());

        //////////////////

        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(6).getLoggerName());
        assertEquals("second higher", capturedLogs.get(6).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1a", capturedLogs.get(7).getLoggerName());
        assertEquals("second lower", capturedLogs.get(7).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1c.2b", capturedLogs.get(8).getLoggerName());
        assertEquals("second higher", capturedLogs.get(8).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c.2b", capturedLogs.get(9).getLoggerName());
        assertEquals("second lower", capturedLogs.get(9).getMessage());

        assertEquals(LOGGING_ROOT + ".top.1a.2a.3c", capturedLogs.get(10).getLoggerName());
        assertEquals("second higher", capturedLogs.get(10).getMessage());
    }

    @Test
    void test_set_branch_viaLogIdentifier_applies_the_same_as_viaLogger() {
        Map<String, Logger> l = setupLoggers();

        Logger directLog = l.get("1b");
        directLog.log(CustomLevel.SEVERE, "direct higher");
        directLog.log(CustomLevel.DATA, "direct lower");

        LogRoot.setBranchLoggingLevel(CustomLevel.INFO, "top.1b");

        directLog.log(CustomLevel.SEVERE, "after higher");
        directLog.log(CustomLevel.DATA, "after lower");


        List<LogRecord> capturedLogs = logCaptor.getAllValues();
        assertEquals(3, capturedLogs.size(), 0);

        assertEquals(LOGGING_ROOT + ".top.1b", capturedLogs.get(0).getLoggerName());
        assertEquals("direct higher", capturedLogs.get(0).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1b", capturedLogs.get(1).getLoggerName());
        assertEquals("direct lower", capturedLogs.get(1).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1b", capturedLogs.get(2).getLoggerName());
        assertEquals("after higher", capturedLogs.get(2).getMessage());
    }

    @Test
    void test_set_branch_viaLogIdentifier_and_prefix_applies_the_same_as_viaLogger() {
        ExtendedLogger withPrefix = LogRoot.createLogger("custPref", "theID");
        withPrefix.setLevel(Level.ALL);

        withPrefix.log(CustomLevel.SEVERE, "before higher");
        withPrefix.log(CustomLevel.DATA, "before lower");

        LogRoot.setBranchLoggingLevel(CustomLevel.INFO, "custPref", "theID");

        withPrefix.log(CustomLevel.SEVERE, "after higher");
        withPrefix.log(CustomLevel.DATA, "after lower");


        List<LogRecord> capturedLogs = logCaptor.getAllValues();
        assertEquals(3, capturedLogs.size(), 0);

        assertEquals(LOGGING_ROOT + ".custPref:theID", capturedLogs.get(0).getLoggerName());
        assertEquals("before higher", capturedLogs.get(0).getMessage());
        assertEquals(LOGGING_ROOT + ".custPref:theID", capturedLogs.get(1).getLoggerName());
        assertEquals("before lower", capturedLogs.get(1).getMessage());
        assertEquals(LOGGING_ROOT + ".custPref:theID", capturedLogs.get(2).getLoggerName());
        assertEquals("after higher", capturedLogs.get(2).getMessage());
    }

    @Test
    void test_set_application_log_level_does_not_affect_global_namespace() {
        Logger otherNamespace = Logger.getLogger("some.other.namespace");
        otherNamespace.setLevel(Level.ALL);

        Logger localAppNameSpace = LogRoot.createLogger(LogRootTest.class);
        localAppNameSpace.setLevel(CustomLevel.ALL);

        LogRoot.setApplicationGlobalLevel(CustomLevel.INFO);

        otherNamespace.log(Level.SEVERE, "other higher");
        otherNamespace.log(Level.FINE, "other lower");
        localAppNameSpace.log(CustomLevel.SEVERE, "local higher");
        localAppNameSpace.log(CustomLevel.DATA, "local lower");

        List<LogRecord> capturedLogs = logCaptor.getAllValues();
        assertEquals(3, capturedLogs.size(), 0);

        assertEquals("some.other.namespace", capturedLogs.get(0).getLoggerName());
        assertEquals("other higher", capturedLogs.get(0).getMessage());
        assertEquals("some.other.namespace", capturedLogs.get(1).getLoggerName());
        assertEquals("other lower", capturedLogs.get(1).getMessage());
        assertEquals(LOGGING_ROOT + ".LogRootTest", capturedLogs.get(2).getLoggerName());
        assertEquals("local higher", capturedLogs.get(2).getMessage());
    }

    @Test
    void test_setting_global_branch_logging_level() {
        Map<String, Logger> l = setupLoggers();
        LogRoot.setGlobalLoggingLevel(CustomLevel.ALL);

        Logger globalRoot = Logger.getLogger("global");
        Logger global1c = Logger.getLogger("global.1c");
        Logger localRoot = l.get("top");
        Logger local1c = l.get("1c");

        globalRoot.log(CustomLevel.SEVERE, "before higher");
        globalRoot.log(CustomLevel.DATA, "before lower");
        global1c.log(CustomLevel.SEVERE, "before higher");
        global1c.log(CustomLevel.DATA, "before lower");
        localRoot.log(CustomLevel.SEVERE, "before higher");
        localRoot.log(CustomLevel.DATA, "before lower");
        local1c.log(CustomLevel.SEVERE, "before higher");
        local1c.log(CustomLevel.DATA, "before lower");

        LogRoot.setGlobalBranchLoggingLevel(CustomLevel.INFO, "global");

        globalRoot.log(CustomLevel.SEVERE, "after higher");
        globalRoot.log(CustomLevel.DATA, "after lower");
        global1c.log(CustomLevel.SEVERE, "after higher");
        global1c.log(CustomLevel.DATA, "after lower");
        localRoot.log(CustomLevel.SEVERE, "after higher");
        localRoot.log(CustomLevel.DATA, "after lower");
        local1c.log(CustomLevel.SEVERE, "after higher");
        local1c.log(CustomLevel.DATA, "after lower");


        List<LogRecord> capturedLogs = logCaptor.getAllValues();
        assertEquals(14, capturedLogs.size(), 0);

        assertEquals("global", capturedLogs.get(0).getLoggerName());
        assertEquals("before higher", capturedLogs.get(0).getMessage());
        assertEquals("global", capturedLogs.get(1).getLoggerName());
        assertEquals("before lower", capturedLogs.get(1).getMessage());
        assertEquals("global.1c", capturedLogs.get(2).getLoggerName());
        assertEquals("before higher", capturedLogs.get(2).getMessage());
        assertEquals("global.1c", capturedLogs.get(3).getLoggerName());
        assertEquals("before lower", capturedLogs.get(3).getMessage());
        assertEquals(LOGGING_ROOT + ".top", capturedLogs.get(4).getLoggerName());
        assertEquals("before higher", capturedLogs.get(4).getMessage());
        assertEquals(LOGGING_ROOT + ".top", capturedLogs.get(5).getLoggerName());
        assertEquals("before lower", capturedLogs.get(5).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(6).getLoggerName());
        assertEquals("before higher", capturedLogs.get(6).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(7).getLoggerName());
        assertEquals("before lower", capturedLogs.get(7).getMessage());

        assertEquals("global", capturedLogs.get(8).getLoggerName());
        assertEquals("after higher", capturedLogs.get(8).getMessage());
        assertEquals("global.1c", capturedLogs.get(9).getLoggerName());
        assertEquals("after higher", capturedLogs.get(9).getMessage());
        assertEquals(LOGGING_ROOT + ".top", capturedLogs.get(10).getLoggerName());
        assertEquals("after higher", capturedLogs.get(10).getMessage());
        assertEquals(LOGGING_ROOT + ".top", capturedLogs.get(11).getLoggerName());
        assertEquals("after lower", capturedLogs.get(11).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(12).getLoggerName());
        assertEquals("after higher", capturedLogs.get(12).getMessage());
        assertEquals(LOGGING_ROOT + ".top.1c", capturedLogs.get(13).getLoggerName());
        assertEquals("after lower", capturedLogs.get(13).getMessage());
    }
}
