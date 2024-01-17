package com.clumd.projects.java_common_utils.logging.common;

import com.clumd.projects.java_common_utils.logging.controllers.ConsoleController;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

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

    @Test
    void test_baked_in_tags_will_default_to_null() {
        assertNull(new ExtendedLogRecord(Level.INFO, "msg").getBakedInTags());
    }

    @Test
    void test_with_controllers_which_should_ignore_defaults_to_null() {
        assertNull(new ExtendedLogRecord(Level.INFO, "msg").getControllersWhichShouldDisregardThisMessage());
    }

    @Test
    void test_accepts_some_baked_in_tags() {
        Set<String> expectedBakedInTags = Set.of("Something", "baked", "in");
        ExtendedLogRecord logRecord = new ExtendedLogRecord(Level.INFO, "msg");
        logRecord.referencingBakedInTags(expectedBakedInTags);

        assertEquals(expectedBakedInTags, logRecord.getBakedInTags());
    }

    @Test
    void test_accepts_some_classes_which_should_ignore() {
        Set<Class<? extends StreamHandler>> classesWhichShouldIgnore = Set.of(ConsoleController.class, FileHandler.class);
        ExtendedLogRecord logRecord = new ExtendedLogRecord(Level.INFO, "msg");
        logRecord.withControllersWhichShouldIgnore(classesWhichShouldIgnore);

        assertEquals(classesWhichShouldIgnore, logRecord.getControllersWhichShouldDisregardThisMessage());
    }

    @Test
    void test_baked_in_does_not_collide_with_instance_tags() {
        Set<String> expectedBakedInTags = Set.of("Something", "baked", "in");
        Set<String> expectedInstanceTags = Set.of("regular", "tags");
        ExtendedLogRecord logRecord = new ExtendedLogRecord(Level.INFO, "msg", expectedInstanceTags);
        logRecord.referencingBakedInTags(expectedBakedInTags);

        assertEquals(expectedBakedInTags, logRecord.getBakedInTags());
        assertEquals(expectedInstanceTags, logRecord.getTags());

    }
}
