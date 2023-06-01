package com.clumd.projects.java_common_utils.logging.common;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.logging.Level;

import static com.clumd.projects.java_common_utils.logging.common.CustomLevel.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomLevelTest {

    private static class ImportantClone extends CustomLevel {

        public ImportantClone() {
            super("Important", IMPORTANT.getPriority());
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
            System.out.println("NONE " + NONE.getLevelFormat() + "HELLO WORLD" + COLOUR_RESET);

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

    @Test
    void test_weak_equals_allows_priority_not_level() {
        CustomLevel newLevel = new CustomLevel("NOT_INFO", 800);

        assertEquals("INFO", INFO.getLevelName());
        assertEquals(800, INFO.getPriority(), 0);

        assertEquals("NOT_INFO", newLevel.getLevelName());
        assertEquals(800, newLevel.getPriority(), 0);

        assertNotEquals(INFO, newLevel);
        assertTrue(INFO.weakEquals(newLevel));
    }

    @Test
    void test_weak_equals_allows_level_not_priority() {
        CustomLevel newLevel = new CustomLevel("INFO", 123);

        assertEquals("INFO", INFO.getLevelName());
        assertEquals(800, INFO.getPriority(), 0);

        assertEquals("INFO", newLevel.getLevelName());
        assertEquals(123, newLevel.getPriority(), 0);

        assertNotEquals(INFO, newLevel);
        assertTrue(INFO.weakEquals(newLevel));
    }

    @Test
    void test_weak_equals_checks_regular_equals_first() {
        assertTrue(INFO.weakEquals(new CustomLevel("INFO", 0)));
    }

    @Test
    void test_weak_equals_fails_if_totally_different() {
        assertFalse(INFO.weakEquals(1));
    }

    @Test
    void test_static_of() {
        assertNotNull(CustomLevel.of("newlevel", 10));
        assertNotNull(CustomLevel.of("newlevel2", 100));
    }

    @Test
    void test_existing_levels_are_equalable() {
        assertTrue(ALL.weakEquals(ALL));
        assertTrue(ALL.weakEquals(Level.ALL));
        assertTrue(SEVERE.weakEquals(Level.SEVERE));
        assertTrue(WARNING.weakEquals(Level.WARNING));
        assertTrue(INFO.weakEquals(Level.INFO));
        assertTrue(CONFIG.weakEquals(Level.CONFIG));
        assertTrue(MINOR.weakEquals(Level.FINE));
        assertTrue(DEBUG.weakEquals(Level.FINER));
        assertTrue(TRACE.weakEquals(Level.FINEST));
        assertTrue(OFF.weakEquals(Level.OFF));
    }

    @Test
    void test_parse_custom_level_uses_custom_version() {
        CustomLevel cl = CustomLevel.parse("InFo");

        assertNotNull(cl);
        assertNotNull(cl.getLevelFormat());
        assertFalse(cl.getLevelFormat().isBlank());
    }

    @Test
    void test_parse_custom_level_non_default_level() {
        CustomLevel cl = CustomLevel.parse("DaTa");

        assertNotNull(cl);
        assertNotNull(cl.getLevelFormat());
        assertFalse(cl.getLevelFormat().isBlank());
    }

    @Test
    void test_parse_non_custom_level_but_still_jul_level() {
        CustomLevel cl = CustomLevel.parse("fiNeSt");

        assertNotNull(cl);
        assertEquals(COLOUR_RESET, cl.getLevelFormat());
    }

    @Test
    void test_cant_just_parse_a_non_level() {
        try {
            CustomLevel.parse("NotLevel");
            fail("The previous method call should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().toLowerCase().contains("bad level"));
        }
    }

    @Test
    void test_once_not_a_level_now_a_level() {
        String levelName = UUID.randomUUID().toString().replaceAll("[0-9-]", "");
        try {
            CustomLevel.parse(levelName);
            fail("The previous method call should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().toLowerCase().contains("bad level"));
        }

        assertNotNull(CustomLevel.of(levelName, 700, Format.BRIGHT_BLUE));

        try {
            CustomLevel cl = CustomLevel.parse(levelName);
            assertNotNull(cl);
            assertNotNull(cl.getLevelFormat());
            assertFalse(cl.getLevelFormat().isBlank());
        } catch (IllegalArgumentException e) {
            fail("Parsing the custom level should have succeeded the second time", e);
        }
    }
}
