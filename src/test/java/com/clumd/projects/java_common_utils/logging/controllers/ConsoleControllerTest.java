package com.clumd.projects.java_common_utils.logging.controllers;

import com.clumd.projects.java_common_utils.logging.api.LoggableData;
import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.core.BasicJsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleControllerTest {

    private ConsoleController controller;
    private UUID runID;
    private String systemId;
    private Map<Long, String> overriddenThreadNames;

    private static class CustomLogFormattedObject implements LoggableData {
        @Override
        public String getFormattedLogData() {
            return "text 12 with \n symbols {\" \": true} ";
        }
    }

    @BeforeEach
    void setup() {
        controller = new ConsoleController(true);
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

        assertTrue(formattedString.startsWith(runID + "    " + systemId + "    "));
        assertTrue(formattedString.endsWith("Message<" + Level.INFO + ">:  " + message + "\n\n"));
        assertTrue(formattedString.contains("(1):Anon/Unknown Thread"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.FINEST, message));

        assertTrue(formattedString.startsWith(runID + "    " + systemId + "    "));
        assertTrue(formattedString.endsWith("Message<" + Level.FINEST + ">:  " + message + "\n\n"));
        assertTrue(formattedString.contains("(1):Anon/Unknown Thread"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.SEVERE, message));

        assertTrue(formattedString.startsWith(runID + "    " + systemId + "    "));
        assertTrue(formattedString.endsWith("Message<" + Level.SEVERE + ">:  " + message + "\n\n"));
        assertTrue(formattedString.contains("(1):Anon/Unknown Thread"));
    }

    @Test
    void test_message_format_without_extras() {
        String message = "blah";
        overriddenThreadNames.put(1L, "some name");
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.FAILURE, message));

        assertTrue(formattedString.startsWith(CustomLevel.FAILURE.getLevelFormat() + runID + "    " + systemId + "    "));
        assertTrue(formattedString.endsWith("Message<" + CustomLevel.FAILURE + ">:  " + CustomLevel.COLOUR_RESET + message + "\n\n"));
        assertTrue(formattedString.contains("(1):some name"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.INFO, message));

        assertTrue(formattedString.startsWith(CustomLevel.INFO.getLevelFormat() + runID + "    " + systemId + "    "));
        assertTrue(formattedString.endsWith("Message<" + CustomLevel.INFO + ">:  " + CustomLevel.COLOUR_RESET + message + "\n\n"));
        assertTrue(formattedString.contains("(1):some name"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.DEBUG, message));

        assertTrue(formattedString.startsWith(CustomLevel.DEBUG.getLevelFormat() + runID + "    " + systemId + "    "));
        assertTrue(formattedString.endsWith("Message<" + CustomLevel.DEBUG + ">:  " + CustomLevel.COLOUR_RESET + message + "\n\n"));
        assertTrue(formattedString.contains("(1):some name"));
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

        assertTrue(formattedString.contains(CustomLevel.WARNING.getLevelFormat() + runID + "    " + systemId + "    "));
        assertTrue(formattedString.contains("    \nMessage<" + CustomLevel.WARNING + ">:  " + CustomLevel.COLOUR_RESET + message + "\n" +
                "Error:  (Throwable) 1st reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (RuntimeException) 2nd reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (IOException) 3rd IO\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (NullPointerException) 4th NPE!\n"));
        assertTrue(formattedString.endsWith("""
                Metadata:  <5> item(s)
                { 1337 }
                {
                  "top": "1",
                  "t": {
                    "s": 2,
                    "second": {
                      <third>
                    }\s
                  },
                  "array": [
                    1,
                    2,
                    3\s
                  ]\s
                }
                { NULL }
                { String }
                {
                text 12 with\s
                 symbols {" ": true}\s
                }
                
                """));
    }

    @Test
    void test_message_format_with_tags() {
        String message = "msg";
        ExtendedLogRecord logRecord = new ExtendedLogRecord(CustomLevel.WARNING, message, Set.of("tag1", "tag2"));

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.contains(CustomLevel.WARNING.getLevelFormat() + runID + "    " + systemId + "    "));
        assertTrue(formattedString.contains("\n[tag1, tag2]\n") || formattedString.contains("\n[tag2, tag1]\n"));
        assertFalse(formattedString.contains("\n[baked, in]\n") || formattedString.contains("\n[in, baked]\n"));
        assertTrue(formattedString.endsWith("\nMessage<" + CustomLevel.WARNING + ">:  " + CustomLevel.COLOUR_RESET + message + "\n\n"));
    }

    @Test
    void test_message_format_with_baked_in_tags() {
        String message = "msg";
        ExtendedLogRecord logRecord = new ExtendedLogRecord(CustomLevel.WARNING, message)
                .referencingBakedInTags(Set.of("baked", "in"));

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.contains(CustomLevel.WARNING.getLevelFormat() + runID + "    " + systemId + "    "));
        assertFalse(formattedString.contains("\n[tag1, tag2]\n") || formattedString.contains("\n[tag2, tag1]\n"));
        assertTrue(formattedString.contains("\n[baked, in]\n") || formattedString.contains("\n[in, baked]\n"));
        assertTrue(formattedString.endsWith("\nMessage<" + CustomLevel.WARNING + ">:  " + CustomLevel.COLOUR_RESET + message + "\n\n"));
    }

    @Test
    void test_message_format_with_regular_and_baked_in_tags() {
        String message = "msg";
        ExtendedLogRecord logRecord = new ExtendedLogRecord(
                CustomLevel.WARNING,
                message,
                Set.of("tag1", "tag2")
        ).referencingBakedInTags(Set.of("baked", "in"));

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.contains(CustomLevel.WARNING.getLevelFormat() + runID + "    " + systemId + "    "));
        assertTrue(formattedString.contains("][tag1, tag2]\n") || formattedString.contains("][tag2, tag1]\n"));
        assertTrue(formattedString.contains("\n[baked, in][") || formattedString.contains("\n[in, baked]["));
        assertTrue(formattedString.endsWith("\nMessage<" + CustomLevel.WARNING + ">:  " + CustomLevel.COLOUR_RESET + message + "\n\n"));
    }
}
