package com.clumd.projects.java_common_utils.logging.controllers;

import com.clumd.projects.java_common_utils.files.FileUtils;
import com.clumd.projects.java_common_utils.logging.api.LoggableData;
import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.core.BasicJsonBuilder;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DenseFileControllerTest {

    private static final String LOGGING_TEST_PATH = "src/test/resources/logging/testLog.log";

    private DenseFileController controller;
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
        controller = new DenseFileController(LOGGING_TEST_PATH, 1000000, 1, false);
        overriddenThreadNames = new HashMap<>();
        controller.acceptLogRootRefs(UUID.randomUUID(), "system id", overriddenThreadNames);
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

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.FINEST, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FINEST]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.SEVERE, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", SEVERE]  " + message));
        assertTrue(formattedString.endsWith("\n"));
    }

    @Test
    void test_message_format_without_extras() {
        String message = "blah";
        overriddenThreadNames.put(1L, "some name");
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.FAILURE, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FAILURE]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.INFO, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.DEBUG, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", DEBUG]  " + message));
        assertTrue(formattedString.endsWith("\n"));
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

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING]  " + message));
        assertTrue(formattedString.contains("\nError:  (Throwable) 1st reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (RuntimeException) 2nd reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (IOException) 3rd IO\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (NullPointerException) 4th NPE!\n"));
        assertTrue(formattedString.contains("\n[1337, {\"top\":\"1\",\"t\":{\"s\":2,\"second\":{<third>}},\"array\":[1,2,3]}, NULL, String, text 12 with    symbols {\" \": true} , NULL]\n"));
        assertTrue(formattedString.endsWith("\n"));
    }

    @Test
    void test_log_actually_written_to_file() throws IOException {
        String message = "blah";
        controller.publish(new LogRecord(Level.INFO, message));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.get(0).startsWith("["));
        assertTrue(fileContents.get(0).contains(", INFO]  " + message));
        assertTrue(fileContents.get(0).endsWith("\n"));
    }

    @Test
    void test_log_tags_to_file_with_only_regular() throws IOException {
        String message = "blah";
        controller.publish(new ExtendedLogRecord(Level.INFO, message, Set.of("tag1", "tag2")));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.get(0).startsWith("["));
        assertTrue(fileContents.get(0).contains(", INFO, [tag1, tag2]]  " + message) || fileContents.get(0).contains(", INFO, [tag2, tag1]]  " + message) );
        assertTrue(fileContents.get(0).endsWith("\n"));
    }


    @Test
    void test_log_tags_to_file_with_baked_in_tags() throws IOException {
        String message = "blah";
        controller.publish(new ExtendedLogRecord(Level.INFO, message)
                .referencingBakedInTags(Set.of("baked")));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.get(0).startsWith("["));
        assertTrue(fileContents.get(0).contains(", INFO, [baked]]  " + message));
        assertTrue(fileContents.get(0).endsWith("\n"));
    }

    @Test
    void test_log_tags_to_file_with_regular_and_baked_in_tags() throws IOException {
        String message = "blah";
        controller.publish(new ExtendedLogRecord(Level.INFO, message, Set.of("tag1"))
                .referencingBakedInTags(Set.of("baked")));

        List<String> fileContents = FileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.get(0).startsWith("["));
        assertTrue(fileContents.get(0).contains(", INFO, [baked], [tag1]]  " + message));
        assertTrue(fileContents.get(0).endsWith("\n"));
    }
}
