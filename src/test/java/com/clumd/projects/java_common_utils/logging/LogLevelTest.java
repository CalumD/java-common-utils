package com.clumd.projects.java_common_utils.logging;

import org.junit.jupiter.api.Test;

import static com.clumd.projects.java_common_utils.logging.LogLevel.COLOUR_RESET;
import static com.clumd.projects.java_common_utils.logging.LogLevel.IMPORTANT;
import static com.clumd.projects.java_common_utils.logging.LogLevel.INFO;
import static com.clumd.projects.java_common_utils.logging.LogLevel.SEVERE;
import static org.junit.jupiter.api.Assertions.*;

class LogLevelTest {

    private static class ImportantClone extends LogLevel {

        public ImportantClone() {
            super("level", IMPORTANT.getPriority());
        }
    }

    private static class ImportantNotClone extends LogLevel {

        public ImportantNotClone() {
            super("level2", IMPORTANT.getPriority() + 1);
        }
    }

    @Test
    void test_two_levels_of_same_priority_hash_the_same() {
        assertEquals(LogLevel.IMPORTANT.hashCode(), new ImportantClone().hashCode(), 0);
    }

    @Test
    void test_name_always_uppercase() {
        LogLevel level = new LogLevel("Critical", 10, LevelFormat.BLUE);
        assertEquals("CRITICAL", level.getLevel());
        assertEquals("CRITICAL", level.toString());
    }

    @Test
    void test_two_levels_of_different_priority_dont_hash_the_same() {
        assertNotEquals(new ImportantNotClone().hashCode(), new ImportantClone().hashCode(), 0);
    }

    @Test
    void test_colours() {
        assertDoesNotThrow(() -> {
            System.out.println("ALL " + LogLevel.ALL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("OFF " + LogLevel.OFF.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("FATAL " + LogLevel.FATAL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("CRITICAL " + LogLevel.CRITICAL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("SEVERE " + LogLevel.SEVERE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("WARNING " + LogLevel.WARNING.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("IMPORTANT " + LogLevel.IMPORTANT.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("INFO " + LogLevel.INFO.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("CONFIG " + LogLevel.CONFIG.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("VERBOSE " + LogLevel.VERBOSE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("DEBUG " + LogLevel.DEBUG.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("TRACE " + LogLevel.TRACE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
        });
    }

    @Test
    @SuppressWarnings("all")
    void test_null_not_equals() {
        assertFalse(LogLevel.IMPORTANT.equals(null));
    }

    @Test
    @SuppressWarnings("all")
    void test_different_class_not_equals() {
        assertFalse(LogLevel.IMPORTANT.equals("IMPORTANT"));
    }

    @Test
    @SuppressWarnings("all")
    void test_different_level_not_equals() {
        assertFalse(LogLevel.IMPORTANT.equals(SEVERE));
    }

    @Test
    @SuppressWarnings("all")
    void test_different_level_does_equals() {
        assertTrue(LogLevel.IMPORTANT.equals(new ImportantClone()));
    }

    @Test
    @SuppressWarnings("all")
    void test_same_level_does_equals() {
        assertTrue(LogLevel.IMPORTANT.equals(IMPORTANT));
    }
}
