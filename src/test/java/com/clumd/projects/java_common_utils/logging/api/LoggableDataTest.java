package com.clumd.projects.java_common_utils.logging.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggableDataTest {

    private static class UsesDefault implements LoggableData {

        @Override
        public String toString() {
            return "Used to string";
        }
    }

    private static class ImplementsCustom implements LoggableData {

        @Override
        public String getFormattedLogData() {
            return "Created some custom implementation";
        }

        @Override
        public String toString() {
            return "Used to string";
        }
    }

    @Test
    void test_default_method_calls_toString(){
        assertEquals("Used to string", new UsesDefault().getFormattedLogData());
    }

    @Test
    void test_custom_implementation_is_okay(){
        assertEquals("Created some custom implementation", new ImplementsCustom().getFormattedLogData());
    }
}
