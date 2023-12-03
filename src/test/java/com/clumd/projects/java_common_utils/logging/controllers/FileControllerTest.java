package com.clumd.projects.java_common_utils.logging.controllers;

import com.clumd.projects.java_common_utils.files.FileUtils;
import com.clumd.projects.java_common_utils.logging.api.LoggableData;
import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.api.Json;
import com.clumd.projects.javajson.api.JsonParser;
import com.clumd.projects.javajson.core.BasicJsonBuilder;
import com.clumd.projects.javajson.exceptions.json.JsonKeyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileControllerTest {

    private static final String LOGGING_TEST_PATH = "src/test/resources/logging/testLog.log";

    private FileController controller;
    private UUID runID;
    private String systemId;
    private Map<Long, String> overriddenThreadNames;

    private static class CustomLogFormattedObject implements LoggableData {
        @Override
        public String getFormattedLogData() {
            return "text 12 with \n symbols {\" \": true} ";
        }
    }

    private static class NullCustomLogFormattedObject implements LoggableData {
        @Override
        public String getFormattedLogData() {
            return null;
        }
    }

    @BeforeEach
    void setup() throws IOException {
        FileUtils.makeContainingDirs(LOGGING_TEST_PATH);
        controller = new FileController(LOGGING_TEST_PATH, 1000000, 1, false);
        runID = UUID.randomUUID();
        systemId = "system id";
        overriddenThreadNames = new HashMap<>();
        controller.acceptLogRootRefs(runID, systemId, overriddenThreadNames);
    }

    @AfterEach
    void tearDown() {
        controller.close();
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
                null,
                "String",
                new CustomLogFormattedObject(),
                new NullCustomLogFormattedObject()
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
        assertTrue(formattedString.contains("\"meta\":[\"1337\",{\"top\":\"1\",\"t\":{\"s\":2,\"second\":{\"third\":{\"fourth\":{\"fifth\":\"value\"}}}},\"array\":[1,2,3]},\"NULL\",\"String\",\"text 12 with    symbols {\\\\\\\" \\\\\\\": true} \",\"NULL\"]"));
        assertTrue(formattedString.contains("\"logger\":\"Anon/Unknown Logger\",\"publisher\":\"" + systemId + "\",\"message\":\"" + message + "\""));
        assertTrue(formattedString.contains("\"error\":[\"Error:  (Throwable) 1st reason\","));
        assertTrue(formattedString.contains("\",\"Nested Reason:  (RuntimeException) 2nd reason\",\""));
        assertTrue(formattedString.contains("\",\"Nested Reason:  (IOException) 3rd IO\",\""));
        assertTrue(formattedString.contains("\",\"Nested Reason:  (NullPointerException) 4th NPE!\",\""));
    }

    @Test
    void test_log_actually_written_to_file() throws IOException {
        String message = "blah";
        controller.publish(new LogRecord(Level.INFO, message));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        Json logWritten = JsonParser.parse(fileContents.get(0));

        assertEquals(1, logWritten.getLongAt("threadID"), 0);
        assertEquals(runID.toString(), logWritten.getStringAt("traceID"));
        assertEquals(Level.INFO.getName(), logWritten.getStringAt("level"));
        assertEquals("Anon/Unknown Logger", logWritten.getStringAt("logger"));
        assertEquals(systemId, logWritten.getStringAt("publisher"));
        assertEquals(message, logWritten.getStringAt("message"));
        assertEquals("Anon/Unknown Thread", logWritten.getStringAt("threadName"));
        assertThrows(JsonKeyException.class, () -> logWritten.getStringAt("tags"));
    }

    @Test
    void test_log_tags_to_file_with_only_regular() throws IOException {
        String message = "blah";
        controller.publish(new ExtendedLogRecord(Level.INFO, message, Set.of("tag1", "tag2")));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        Json logWritten = JsonParser.parse(fileContents.get(0));

        assertDoesNotThrow(() -> logWritten.getArrayAt("tags"));
        assertEquals(2, logWritten.getArrayAt("tags").size(), 0);
        assertTrue(logWritten.getValueAt("tags[0]").equals("tag1") || logWritten.getValueAt("tags[0]").equals("tag2"));
        assertTrue(logWritten.getValueAt("tags[1]").equals("tag1") || logWritten.getValueAt("tags[1]").equals("tag2"));
    }


    @Test
    void test_log_tags_to_file_with_baked_in_tags() throws IOException {
        String message = "blah";
        controller.publish(new ExtendedLogRecord(Level.INFO, message)
                .referencingBakedInTags(Set.of("baked")));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        Json logWritten = JsonParser.parse(fileContents.get(0));

        assertDoesNotThrow(() -> logWritten.getArrayAt("tags"));
        assertEquals(1, logWritten.getArrayAt("tags").size(), 0);
        assertEquals("baked", logWritten.getValueAt("tags[0]"));
    }

    @Test
    void test_log_tags_to_file_with_regular_and_baked_in_tags() throws IOException {
        String message = "blah";
        controller.publish(new ExtendedLogRecord(Level.INFO, message, Set.of("tag1"))
                .referencingBakedInTags(Set.of("baked")));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        Json logWritten = JsonParser.parse(fileContents.get(0));

        assertDoesNotThrow(() -> logWritten.getArrayAt("tags"));
        assertEquals(2, logWritten.getArrayAt("tags").size(), 0);
        assertTrue(logWritten.getValueAt("tags[0]").equals("tag1") || logWritten.getValueAt("tags[0]").equals("baked"));
        assertTrue(logWritten.getValueAt("tags[1]").equals("tag1") || logWritten.getValueAt("tags[1]").equals("baked"));
    }
}
