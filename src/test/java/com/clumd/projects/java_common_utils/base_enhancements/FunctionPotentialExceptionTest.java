package com.clumd.projects.java_common_utils.base_enhancements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class FunctionPotentialExceptionTest {

    @Test
    void test_function_can_throw_checked_exception() {
        try {
            FunctionPotentialException<String, String, CheckedException> func = (input) -> {
                throw new CheckedException("test message");
            };
            func.apply("my input message");

            fail("The previous method call should have thrown an exception.");
        } catch (CheckedException e) {
            assertEquals("test message", e.getMessage());
        }
    }

    @Test
    void test_function_can_throw_unchecked_exception() {
        try {
            FunctionPotentialException<String, String, UncheckedException> func = (input) -> {
                throw new UncheckedException("test message");
            };
            func.apply("my input message");

            fail("The previous method call should have thrown an exception.");
        } catch (UncheckedException e) {
            assertEquals("test message", e.getMessage());
        }
    }

    @Test
    void test_function_does_not_need_to_throw_exception() {
        FunctionPotentialException<String, String, UncheckedException> func = (input) -> input + " | " + input;
        String actual = func.apply("test message");

        assertEquals("test message | test message", actual);
    }

    private static class CheckedException extends UnwrappableException {
        public CheckedException(String reason) {
            super(reason);
        }
    }

    private static class UncheckedException extends UnwrappableRuntimeException {
        public UncheckedException(String reason) {
            super(reason);
        }
    }
}
