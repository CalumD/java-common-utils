package com.clumd.projects.java_common_utils.logging;

import org.junit.jupiter.api.Test;

import static com.clumd.projects.java_common_utils.logging.LogLevel.*;
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
        assertEquals(IMPORTANT.hashCode(), new ImportantClone().hashCode(), 0);
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
            System.out.println("ALL " + ALL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("OFF " + OFF.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("FATAL " + FATAL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("CRITICAL " + CRITICAL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("SEVERE " + SEVERE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("WARNING " + WARNING.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("IMPORTANT " + IMPORTANT.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("INFO " + INFO.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("CONFIG " + CONFIG.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("VERBOSE " + VERBOSE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("DEBUG " + DEBUG.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("TRACE " + TRACE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
        });
    }

    @Test
    @SuppressWarnings("all")
    void test_null_not_equals() {
        assertFalse(IMPORTANT.equals(null));
    }

    @Test
    @SuppressWarnings("all")
    void test_different_class_not_equals() {
        assertFalse(IMPORTANT.equals("IMPORTANT"));
    }

    @Test
    @SuppressWarnings("all")
    void test_different_level_not_equals() {
        assertFalse(IMPORTANT.equals(SEVERE));
    }

    @Test
    @SuppressWarnings("all")
    void test_different_level_does_equals() {
        assertTrue(IMPORTANT.equals(new ImportantClone()));
    }

    @Test
    @SuppressWarnings("all")
    void test_same_level_does_equals() {
        assertTrue(IMPORTANT.equals(IMPORTANT));
    }
}
