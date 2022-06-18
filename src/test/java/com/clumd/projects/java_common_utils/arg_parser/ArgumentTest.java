package com.clumd.projects.java_common_utils.arg_parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentTest {

    private Argument<String> argForTest;


    @BeforeEach
    void setup() {
        argForTest = Argument.<String>builder().build();
    }

    @Test
    void test_unique_id_set() {
        Argument<String> args = Argument.<String>builder().build();
        assertEquals(Integer.MIN_VALUE, args.getUniqueId(), 0);
        args = Argument.<String>builder().uniqueId(123).build();
        assertEquals(123, args.getUniqueId(), 0);
    }

    @Test
    void test_short_options_defaults() {
        assertTrue(argForTest.getShortOptions().isEmpty());
    }

    @Test
    void test_long_options_defaults() {
        assertTrue(argForTest.getLongOptions().isEmpty());
    }

    @Test
    void test_has_value_defaults() {
        assertFalse(argForTest.hasValue());
    }

    @Test
    void test_value_is_optional_defaults() {
        assertFalse(argForTest.valueIsOptional());
    }

    @Test
    void test_value_is_optional_even_if_has_value() {
        argForTest = Argument.<String>builder().hasValue(true).build();
        assertTrue(argForTest.hasValue());
        assertFalse(argForTest.valueIsOptional());
    }

    @Test
    void test_description_is_empty() {
        assertEquals("", argForTest.getDescription());
    }

    @Test
    void test_validation_function_null() {
        assertNull(argForTest.getValidationFunction());
    }

    @Test
    void test_conversion_function_null() {
        assertNull(argForTest.getConversionFunction());
    }

    @Test
    void test_argument_result_null() {
        assertNull(argForTest.getArgumentResult());
    }

    @Test
    void test_argument_result_can_be_set() {
        argForTest.setArgumentResult("something");
        assertEquals("something", argForTest.getArgumentResult());
    }

    @Test
    void test_attempting_conversion_can_FAIL_due_to_null() {
        Argument<Integer> intArg = Argument.<Integer>builder().build();

        assertThrows(
                NullPointerException.class,
                () -> intArg.attemptValueConversion("123")
        );
    }

    @Test
    void test_attempting_conversion_can_FAIL_due_to_error_in_conversion() {
        Argument<Integer> intArg = Argument
                .<Integer>builder()
                .conversionFunction(Integer::parseInt)
                .build();

        assertThrows(
                NumberFormatException.class,
                () -> intArg.attemptValueConversion("abc")
        );
    }

    @Test
    void test_attempting_conversion_should_pass() {
        Argument<Integer> intArg = Argument
                .<Integer>builder()
                .conversionFunction(Integer::parseInt)
                .build();

        intArg.attemptValueConversion("123");

        assertEquals(123, intArg.getArgumentResult(), 0);
    }

    @Test
    void test_validate_value_can_fail_with_validation() {
        argForTest = Argument
                .<String>builder()
                .validationFunction((str) -> str.equals("world"))
                .build();

        argForTest.setArgumentResult("hello");

        assertFalse(argForTest.validateValue());
    }

    @Test
    void test_validate_value_can_pass() {
        argForTest = Argument
                .<String>builder()
                .validationFunction((str) -> str.equals("world"))
                .build();

        argForTest.setArgumentResult("world");

        assertTrue(argForTest.validateValue());
    }

    @Test
    void test_validate_value_defaults_true() {
        argForTest.setArgumentResult("world");
        assertTrue(argForTest.validateValue());
    }
}
