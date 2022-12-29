package com.clumd.projects.java_common_utils.logging.controllers;

import com.clumd.projects.java_common_utils.logging.api.LoggableData;
import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.core.BasicJsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DenseConsoleControllerTest {

    private DenseConsoleController controller;

    private static class CustomLogFormattedObject implements LoggableData {
        @Override
        public String getFormattedLogData() {
            return "text 12 with \n symbols {\" \": true} ";
        }
    }

    @BeforeEach
    void setup() {
        controller = new DenseConsoleController();
        controller.acceptLogRootRefs(UUID.randomUUID(), "system id", new HashMap<>());
    }

    @Test
    void test_message_format_without_extras_or_colour() {
        String message = "blah";
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.INFO, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO] blah\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.FINEST, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FINEST] blah\n"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.SEVERE, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", SEVERE] blah\n"));
    }

    @Test
    void test_message_format_without_extras() {
        String message = "blah";
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.FAILURE, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FAILURE" + CustomLevel.COLOUR_RESET + "] blah"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.INFO, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO" + CustomLevel.COLOUR_RESET + "] blah"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.DEBUG, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", DEBUG" + CustomLevel.COLOUR_RESET + "] blah"));
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
                new CustomLogFormattedObject()
        };

        String message = "Here is some warning due to the attached";
        LogRecord logRecord = new LogRecord(CustomLevel.WARNING, message);
        logRecord.setThrown(quadNestedThrowable);
        logRecord.setParameters(logParams);

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING" + CustomLevel.COLOUR_RESET + "] Here is some warning due to the attached\n" +
                "Error:  (Throwable) 1st reason\n  "));
        assertTrue(formattedString.contains("\nNested Reason:  (RuntimeException) 2nd reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (IOException) 3rd IO\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (NullPointerException) 4th NPE!\n"));
        assertTrue(formattedString.endsWith("""

                Metadata:  <5> item(s)
                { 1337 }
                {"top":"1","t":{"s":2,"second":{<third>}},"array":[1,2,3]}
                { NULL }
                { String }
                { text 12 with\s
                 symbols {" ": true}  }
                """));
    }

    @Test
    void test_message_format_with_tags() {
        String message = "msg";
        ExtendedLogRecord logRecord = new ExtendedLogRecord(CustomLevel.WARNING, message, Set.of("tag1", "tag2"));

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING, [tag2, tag1]" + CustomLevel.COLOUR_RESET + "] msg\n")
                || formattedString.contains(", WARNING, [tag1, tag2]" + CustomLevel.COLOUR_RESET + "] msg\n"));
    }
}
