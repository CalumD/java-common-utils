package com.clumd.projects.java_common_utils.logging.common;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExtendedLogRecordTest {

    @Test
    void test_passthrough_constructor_doesnt_throw() {
        assertDoesNotThrow(() -> {
            new ExtendedLogRecord(Level.INFO, "msg");
            new ExtendedLogRecord(CustomLevel.INFO, "msg");
        });
    }

    @Test
    void test_passthrough_constructor_defaults_to_null_tags() {
        assertNull(new ExtendedLogRecord(Level.INFO, "msg").getTags());
    }

    @Test
    void test_tags_accepts() {
        Set<String> tags = Set.of("val1", "val2", "val3");
        assertEquals(tags, new ExtendedLogRecord(Level.INFO, "msg", tags).getTags());
    }

    @Test
    void test_tag_accepts() {
        assertEquals(Set.of("val1"), new ExtendedLogRecord(Level.INFO, "msg", "val1").getTags());
    }
}
