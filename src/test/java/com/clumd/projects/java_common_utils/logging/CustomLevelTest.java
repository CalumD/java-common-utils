package com.clumd.projects.java_common_utils.logging;

import org.junit.jupiter.api.Test;

import static com.clumd.projects.java_common_utils.logging.CustomLevel.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomLevelTest {

    private static class ImportantClone extends CustomLevel {

        public ImportantClone() {
            super("level", IMPORTANT.getPriority());
        }
    }

    private static class ImportantNotClone extends CustomLevel {

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
        CustomLevel level = new CustomLevel("Critical", 10, Format.BLUE);
        assertEquals("CRITICAL", level.getLevelName());
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

            System.out.println("SHUTDOWN " + SHUTDOWN.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("EMERGENCY " + EMERGENCY.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("FATAL " + FATAL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("CRITICAL " + CRITICAL.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("SEVERE " + SEVERE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("ERROR " + ERROR.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("FAILURE " + FAILURE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("WARNING " + WARNING.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("IMPORTANT " + IMPORTANT.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("NOTIFY " + NOTIFICATION.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("INFO " + INFO.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("SUCCESS " + SUCCESS.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("CONFIG " + CONFIG.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("DATA " + DATA.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("VERBOSE " + VERBOSE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("MINOR " + MINOR.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("DEBUG " + DEBUG.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
            System.out.println("TESTING " + TESTING.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);
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
