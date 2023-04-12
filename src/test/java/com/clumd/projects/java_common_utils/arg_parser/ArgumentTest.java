package com.clumd.projects.java_common_utils.arg_parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

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
        assertEquals("_._._._._._._._._._.", args.getUniqueId());
        args = Argument.<String>builder().uniqueId("123").build();
        assertEquals("123", args.getUniqueId());
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

        try {
            intArg.attemptValueConversion("abc");
            fail("The previous method call should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getCause());
            assertNull(e.getCause().getCause());
            assertEquals(NumberFormatException.class, e.getCause().getClass());
        }
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

    @Test
    void test_there_is_a_friendly_error_if_default_value_doesnt_pass_validation_function() {
        // The argument below is broken because the validation function says the number must be between 10 and 30,
        // but the value is optional, and the default value is not provided (so will be interpreted as null)
        // Therefore when we ask for argument -a, with no argument, it will try to validate "null", which should break.
        Argument<Integer> a = Argument.<Integer>builder()
                .uniqueId("1")
                .description(
                        "A number between 10 and 30"
                )
                .shortOptions(Set.of('a'))
                .longOptions(Set.of("alpha"))
                .hasValue(true)
                .valueIsOptional(true)
                .conversionFunction(Integer::parseInt)
                .validationFunction(i -> i > 10 && i < 30)
                .build();

        try {
            a.validateValue();
            fail("The previous method call should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            assertEquals("Argument with ID {1} failed to validate. " +
                    "Check supplied value, or that the default value is valid for the given Validation function, " +
                    "if providing an argument is optional.", e.getMessage());
        }
    }

    @Test
    void test_default_value_returned_if_no_value_to_convert() {
        Argument<Integer> intArg = Argument
                .<Integer>builder()
                .defaultValue(987)
                .build();

        intArg.attemptValueConversion(null);

        assertEquals(987, intArg.getArgumentResult(), 0);
    }

    @Test
    void test_conversion_throws() {
        // check regular argument conversion is successful
        Argument.ArgumentBuilder<Integer> intArg = Argument
                .<Integer>builder()
                .conversionFunction(strValIn -> 1337);

        Argument<Integer> result = intArg.build();
        result.attemptValueConversion("1337");
        assertEquals(1337, result.getArgumentResult(), 0);


        // check argument conversion causes a checked exception
        result = intArg
                .conversionFunction(strValIn -> {throw new Exception("some test reason");})
                .build();
        try {
            result.attemptValueConversion("1337");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertNotNull(e.getCause());
            assertNull(e.getCause().getCause());
            assertEquals("some test reason", e.getCause().getMessage());
        }


        // Check value validation passes
        result = intArg
                .conversionFunction(Integer::parseInt)
                .validationFunction(intIn -> intIn > 1000 && intIn < 1500)
                .build();
        result.attemptValueConversion("1337");
        assertTrue(result.validateValue());
        result.attemptValueConversion("999");
        assertFalse(result.validateValue());
        result.attemptValueConversion("1501");
        assertFalse(result.validateValue());


        // Check value validation returns a null
        result = intArg
                .conversionFunction(Integer::parseInt)
                .validationFunction(intIn -> null)
                .build();
        result.attemptValueConversion("1337");
        try {
            result.validateValue();
            fail("The previous method call should have thrown an exception.");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertNotNull(e.getCause());
            assertNull(e.getCause().getCause());
            assertTrue(e.getCause().getMessage().contains("Validation function returned a null instead of true/false"));
        }


        // Check value validation throws an Exception
        result = intArg
                .conversionFunction(Integer::parseInt)
                .validationFunction(intIn -> {throw new Exception("some test reason");})
                .build();
        result.attemptValueConversion("1337");
        try {
            result.validateValue();
            fail("The previous method call should have thrown an exception.");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertNotNull(e.getCause());
            assertNull(e.getCause().getCause());
            assertEquals("some test reason", e.getCause().getMessage());
        }
    }

    @Test
    void test_toString() {
        Argument<Integer> intArg = Argument
                .<Integer>builder()
                .conversionFunction(Integer::parseInt)
                .build();

        intArg.attemptValueConversion("357");

        assertEquals("357", intArg.toString());
    }

    @Test
    void test_toString_when_value_null() {
        Argument<Integer> intArg = Argument
                .<Integer>builder()
                .conversionFunction(Integer::parseInt)
                .build();

        assertEquals("*no value*", intArg.toString());
    }
}
