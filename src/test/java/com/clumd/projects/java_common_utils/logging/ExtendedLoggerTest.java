package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import com.clumd.projects.java_common_utils.logging.common.ExtendedLogRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ExtendedLoggerTest {

    private ExtendedLogRecord capturedLogRecord;

    private ExtendedLogger extendedLogger;

    @Test
    void test_LevelString() {
        extendedLogger.log(CustomLevel.TESTING, "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @BeforeEach
    void setup() {
        extendedLogger = new ExtendedLoggerVerification("test.extended.logger");
        extendedLogger.setLevel(Level.ALL);
    }

    @Test
    void test_LevelTagString() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsString() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelSupplier() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagSupplier() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsSupplier() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelStringParam() {
        extendedLogger.log(CustomLevel.TESTING, "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagStringParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsStringParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelSupplierParam() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagSupplierParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsSupplierParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelStringParams() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagStringParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsStringParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelSupplierParams() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagSupplierParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsSupplierParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelStringThrow() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNotNull(capturedLogRecord.getThrown());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagStringThrow() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsStringThrow() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelSupplierThrow() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagSupplierThrow() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsSupplierThrow() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelStringThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagStringThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsStringThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelSupplierThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagSupplierThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsSupplierThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelStringThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagStringThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsStringThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelSupplierThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagSupplierThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsSupplierThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_verifyThatBakedInLogsAreAppliedToAMessageWithNoOtherTags() {
        Set<String> expectedTags = Set.of("baked", "in", "tags");
        extendedLogger = new ExtendedLoggerVerification("test.extended.logger", expectedTags);
        extendedLogger.setLevel(Level.ALL);

        extendedLogger.log(CustomLevel.TESTING, "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertEquals(expectedTags, capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_verifyThatBakedInLogsAreAppliedToAMessageWithOtherTags() {
        Set<String> expectedTags = Set.of("baked");
        extendedLogger = new ExtendedLoggerVerification("test.extended.logger", expectedTags);
        extendedLogger.setLevel(Level.ALL);

        extendedLogger.log(CustomLevel.TESTING, Set.of("case-by-case"), "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("case-by-case"), capturedLogRecord.getTags());
        assertEquals(expectedTags, capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    private class ExtendedLoggerVerification extends ExtendedLogger {
        protected ExtendedLoggerVerification(String name) {
            super(name);
        }
        protected ExtendedLoggerVerification(String name, Set<String> bakedInTags) {
            super(name, bakedInTags);
        }

        @Override
        public void log(LogRecord record) {
            capturedLogRecord = (ExtendedLogRecord) record;
        }
    }
}
