package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExtendedSlf4jLoggerTest {

    private static final String LOGGER_NAME = "My testing name";

    private static final String TEST_MESSAGE_0 = "test message";
    private static final String TEST_MESSAGE_1_FORMAT = "formatted {}";
    private static final String TEST_MESSAGE_1 = "one";
    private static final String TEST_MESSAGE_1_EXPECTED = "formatted one";
    private static final String TEST_MESSAGE_2_FORMAT = "formatted {}, {}";
    private static final String TEST_MESSAGE_2 = "two";
    private static final String TEST_MESSAGE_2_EXPECTED = "formatted two, two";
    private static final String TEST_MESSAGE_N_FORMAT = "formatted {}, {}, {}";
    private static final String TEST_MESSAGE_N = "more";
    private static final String TEST_MESSAGE_N_EXPECTED = "formatted more, more, more";

    private static final RuntimeException TEST_EXCEPTION = new RuntimeException("Deliberate test");
    private static final String TEST_MARKER_ID = "marker ID";
    private static final Marker TESTING_MARKER = new BasicMarkerFactory().getMarker(TEST_MARKER_ID);

    private ExtendedSlf4jLogger extendedSlf4jLogger;
    private ExtendedLogRecord capturedLogRecord;

    @BeforeEach
    void setUp() {
        extendedSlf4jLogger = new ExtendedLoggerVerification(LOGGER_NAME);
        extendedSlf4jLogger.setLevel(Level.ALL);
    }

    @Test
    void isTraceEnabled() {
        assertTrue(extendedSlf4jLogger.isTraceEnabled());
    }

    @Test
    void testIsTraceEnabled() {
        assertTrue(extendedSlf4jLogger.isTraceEnabled(TESTING_MARKER));
    }

    @Test
    void trace() {
        extendedSlf4jLogger.trace(TEST_MESSAGE_0);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testTrace() {
        extendedSlf4jLogger.trace(TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testTrace1() {
        extendedSlf4jLogger.trace(TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testTrace2() {
        extendedSlf4jLogger.trace(TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testTrace3() {
        extendedSlf4jLogger.trace(TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testTrace4() {
        extendedSlf4jLogger.trace(TESTING_MARKER, TEST_MESSAGE_0);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testTrace5() {
        extendedSlf4jLogger.trace(TESTING_MARKER, TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testTrace6() {
        extendedSlf4jLogger.trace(TESTING_MARKER, TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testTrace7() {
        extendedSlf4jLogger.trace(TESTING_MARKER, TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testTrace8() {
        extendedSlf4jLogger.trace(TESTING_MARKER, TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void isDebugEnabled() {
        assertTrue(extendedSlf4jLogger.isDebugEnabled());
    }

    @Test
    void testIsDebugEnabled() {
        assertTrue(extendedSlf4jLogger.isDebugEnabled(TESTING_MARKER));
    }

    @Test
    void debug() {
        extendedSlf4jLogger.debug(TEST_MESSAGE_0);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testDebug() {
        extendedSlf4jLogger.debug(TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testDebug1() {
        extendedSlf4jLogger.debug(TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testDebug2() {
        extendedSlf4jLogger.debug(TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testDebug3() {
        extendedSlf4jLogger.debug(TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testDebug4() {
        extendedSlf4jLogger.debug(TESTING_MARKER, TEST_MESSAGE_0);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testDebug5() {
        extendedSlf4jLogger.debug(TESTING_MARKER, TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testDebug6() {
        extendedSlf4jLogger.debug(TESTING_MARKER, TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testDebug7() {
        extendedSlf4jLogger.debug(TESTING_MARKER, TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testDebug8() {
        extendedSlf4jLogger.debug(TESTING_MARKER, TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void isInfoEnabled() {
        assertTrue(extendedSlf4jLogger.isInfoEnabled());
    }

    @Test
    void testIsInfoEnabled() {
        assertTrue(extendedSlf4jLogger.isInfoEnabled(TESTING_MARKER));
    }

    @Test
    void info() {
        extendedSlf4jLogger.info(TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testInfo() {
        extendedSlf4jLogger.info(TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testInfo1() {
        extendedSlf4jLogger.info(TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testInfo2() {
        extendedSlf4jLogger.info(TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testInfo3() {
        extendedSlf4jLogger.info(TESTING_MARKER, TEST_MESSAGE_0);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testInfo4() {
        extendedSlf4jLogger.info(TESTING_MARKER, TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testInfo5() {
        extendedSlf4jLogger.info(TESTING_MARKER, TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testInfo6() {
        extendedSlf4jLogger.info(TESTING_MARKER, TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testInfo7() {
        extendedSlf4jLogger.info(TESTING_MARKER, TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void isWarnEnabled() {
        assertTrue(extendedSlf4jLogger.isWarnEnabled());
    }

    @Test
    void testIsWarnEnabled() {
        assertTrue(extendedSlf4jLogger.isWarnEnabled(TESTING_MARKER));
    }

    @Test
    void warn() {
        extendedSlf4jLogger.warn(TEST_MESSAGE_0);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testWarn() {
        extendedSlf4jLogger.warn(TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testWarn1() {
        extendedSlf4jLogger.warn(TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testWarn2() {
        extendedSlf4jLogger.warn(TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testWarn3() {
        extendedSlf4jLogger.warn(TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testWarn4() {
        extendedSlf4jLogger.warn(TESTING_MARKER, TEST_MESSAGE_0);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testWarn5() {
        extendedSlf4jLogger.warn(TESTING_MARKER, TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testWarn6() {
        extendedSlf4jLogger.warn(TESTING_MARKER, TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testWarn7() {
        extendedSlf4jLogger.warn(TESTING_MARKER, TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testWarn8() {
        extendedSlf4jLogger.warn(TESTING_MARKER, TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void isErrorEnabled() {
        assertTrue(extendedSlf4jLogger.isErrorEnabled());
    }

    @Test
    void testIsErrorEnabled() {
        assertTrue(extendedSlf4jLogger.isErrorEnabled(TESTING_MARKER));
    }

    @Test
    void error() {
        extendedSlf4jLogger.error(TEST_MESSAGE_0);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testError() {
        extendedSlf4jLogger.error(TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testError1() {
        extendedSlf4jLogger.error(TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testError2() {
        extendedSlf4jLogger.error(TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testError3() {
        extendedSlf4jLogger.error(TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
    }

    @Test
    void testError4() {
        extendedSlf4jLogger.error(TESTING_MARKER, TEST_MESSAGE_0);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testError5() {
        extendedSlf4jLogger.error(TESTING_MARKER, TEST_MESSAGE_1_FORMAT, TEST_MESSAGE_1);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_1_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testError6() {
        extendedSlf4jLogger.error(TESTING_MARKER, TEST_MESSAGE_2_FORMAT, TEST_MESSAGE_2, TEST_MESSAGE_2);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_2_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testError7() {
        extendedSlf4jLogger.error(TESTING_MARKER, TEST_MESSAGE_N_FORMAT, TEST_MESSAGE_N, TEST_MESSAGE_N, TEST_MESSAGE_N);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_N_EXPECTED, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    @Test
    void testError8() {
        extendedSlf4jLogger.error(TESTING_MARKER, TEST_MESSAGE_0, TEST_EXCEPTION);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals(LOGGER_NAME, capturedLogRecord.getLoggerName());
        assertEquals(TEST_MESSAGE_0, capturedLogRecord.getMessage());
        assertEquals(Set.of(TEST_MARKER_ID), capturedLogRecord.getTags());
    }

    private class ExtendedLoggerVerification extends ExtendedSlf4jLogger {
        protected ExtendedLoggerVerification(String name) {
            super(name);
        }

        @Override
        public void log(LogRecord record) {
            capturedLogRecord = (ExtendedLogRecord) record;
        }
    }
}
