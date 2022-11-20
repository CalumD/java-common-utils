package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.javajson.core.BasicJsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileControllerTest {

    private static final String LOGGING_TEST_PATH = "src/test/resources/logging/testLog";

    private FileController controller;
    private UUID runID;
    private String systemId;
    private Map<Long, String> overriddenThreadNames;

    @BeforeEach
    void setup() throws IOException {
        controller = new FileController(LOGGING_TEST_PATH, 1000000, 1, false);
        runID = UUID.randomUUID();
        systemId = "system id";
        overriddenThreadNames = new HashMap<>();
        controller.acceptLogRootRefs(runID, systemId, overriddenThreadNames);
    }

    @Test
    void test_message_format_without_extras_or_colour() {
        String message = "blah";
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.INFO, message));

        assertTrue(formattedString.startsWith("{\"threadID\":1,\"traceID\":\"" + runID + "\",\"dateTime\":\""));
        assertTrue(formattedString.contains("\",\"level\":\"" + Level.INFO + "\",\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\",\"threadName\":\"Anon/Unknown Thread\",\"machineDateTime\":"));
        assertTrue(formattedString.endsWith("}\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.FINEST, message));

        assertTrue(formattedString.startsWith("{\"threadID\":1,\"traceID\":\"" + runID + "\",\"dateTime\":\""));
        assertTrue(formattedString.contains("\",\"level\":\"" + Level.FINEST + "\",\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\",\"threadName\":\"Anon/Unknown Thread\",\"machineDateTime\":"));
        assertTrue(formattedString.endsWith("}\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.SEVERE, message));

        assertTrue(formattedString.startsWith("{\"threadID\":1,\"traceID\":\"" + runID + "\",\"dateTime\":\""));
        assertTrue(formattedString.contains("\",\"level\":\"" + Level.SEVERE + "\",\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\",\"threadName\":\"Anon/Unknown Thread\",\"machineDateTime\":"));
        assertTrue(formattedString.endsWith("}\n"));
    }

    @Test
    void test_message_format_without_extras() {
        String message = "blah";
        overriddenThreadNames.put(1L, "some name");
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.FAILURE, message));

        assertTrue(formattedString.startsWith("{\"threadID\":1,\"traceID\":\"" + runID + "\",\"dateTime\":\""));
        assertTrue(formattedString.contains("\",\"level\":\"" + CustomLevel.FAILURE.getLevelName() + "\",\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\",\"threadName\":\"some name\",\"machineDateTime\":"));
        assertTrue(formattedString.endsWith("}\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.INFO, message));

        assertTrue(formattedString.startsWith("{\"threadID\":1,\"traceID\":\"" + runID + "\",\"dateTime\":\""));
        assertTrue(formattedString.contains("\",\"level\":\"" + CustomLevel.INFO.getLevelName() + "\",\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\",\"threadName\":\"some name\",\"machineDateTime\":"));
        assertTrue(formattedString.endsWith("}\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.DEBUG, message));

        assertTrue(formattedString.startsWith("{\"threadID\":1,\"traceID\":\"" + runID + "\",\"dateTime\":\""));
        assertTrue(formattedString.contains("\",\"level\":\"" + CustomLevel.DEBUG.getLevelName() + "\",\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\",\"threadName\":\"some name\",\"machineDateTime\":"));
        assertTrue(formattedString.endsWith("}\n"));
    }

    @Test
    void test_message_format_with_throwables_and_data() {
        Throwable quadNestedThrowable = new Throwable(
                "1st reason",
                new RuntimeException("2nd reason",
                        new IOException("3rd IO",
                                new NullPointerException("4th NPE!")))
        );
        Object[] logParams = new Object[]{
                1337,
                BasicJsonBuilder
                        .getBuilder()
                        .addString("top", "1")
                        .addLong("t.s", 2)
                        .addLong("array[]", 1)
                        .addLong("array[]", 2)
                        .addLong("array[]", 3)
                        .addString("t.second.third.fourth.fifth", "value")
                        .build(),
                "String"
        };

        String message = "Here is some warning due to the attached";
        LogRecord logRecord = new LogRecord(CustomLevel.WARNING, message);
        logRecord.setThrown(quadNestedThrowable);
        logRecord.setParameters(logParams);

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("{\"threadID\":1,\"traceID\":\"" + runID + "\",\"dateTime\":\""));
        assertTrue(formattedString.contains("\"level\":\"" + CustomLevel.WARNING.getLevelName() + "\""));
        assertTrue(formattedString.contains("\"meta\":[\"1337\",{\"top\":\"1\",\"t\":{\"s\":2,\"second\":{\"third\":{\"fourth\":{\"fifth\":\"value\"}}}},\"array\":[1,2,3]},\"String\"]"));
        assertTrue(formattedString.contains("\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\""));
        assertTrue(formattedString.contains("\"error\":[\"Error:  (Throwable) 1st reason\","));
        assertTrue(formattedString.contains("\",\"Nested Reason:  (RuntimeException) 2nd reason\",\""));
        assertTrue(formattedString.contains("\",\"Nested Reason:  (IOException) 3rd IO\",\""));
        assertTrue(formattedString.contains("\",\"Nested Reason:  (NullPointerException) 4th NPE!\",\""));
    }
}
