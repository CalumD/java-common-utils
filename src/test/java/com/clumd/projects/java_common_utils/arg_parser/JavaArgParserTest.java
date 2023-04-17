package com.clumd.projects.java_common_utils.arg_parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JavaArgParserTest {

    private CLIArgParser cliArgParser;

    @BeforeEach
    void setup() {
        cliArgParser = new JavaArgParser();
    }

    @Test
    void test_empty_cli_returns_no_args() throws ParseException {
        assertTrue(cliArgParser.parseFromCLI(
                        List.of(
                                Argument
                                        .builder()
                                        .uniqueId("my named arg")
                                        .shortOptions(Set.of('a'))
                                        .build()
                        ),
                        new String[]{""}
                ).isEmpty()
        );
    }

    @Test
    void test_empty_args_throws() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(),
                    new String[]{""}
            );
            fail("The above code should have thrown an exception.");
        } catch (ParseException e) {
            assertTrue(e.getMessage()
                    .contains("No arguments provided to parse for."));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void test_duplicate_argument_ids_is_not_allowed() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument.builder().uniqueId("my named arg").build(),
                            Argument.builder().uniqueId("my named arg").build()
                    ),
                    new String[]{""}
            );
            fail("The above code should have thrown an exception.");
        } catch (ParseException e) {
            assertTrue(e.getMessage()
                    .contains("Either, provided more than one CLI Argument with the same ID, " +
                            "these must be unique; or no ID was provided."));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void test_duplicate_short_args_not_allowed() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument.builder().uniqueId("my named arg 1").shortOptions(Set.of('a')).build(),
                            Argument.builder().uniqueId("my named arg 2").shortOptions(Set.of('a')).build()
                    ),
                    new String[]{""}
            );
            fail("The above code should have thrown an exception.");
        } catch (ParseException e) {
            assertTrue(e.getMessage()
                    .contains("Argument short-option {a} is used by multiple Arguments"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void test_duplicate_long_args_not_allowed() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument.builder().uniqueId("my named arg 1").longOptions(Set.of("opt")).build(),
                            Argument.builder().uniqueId("my named arg 2").longOptions(Set.of("opt")).build()
                    ),
                    new String[]{""}
            );
            fail("The above code should have thrown an exception.");
        } catch (ParseException e) {
            assertTrue(e.getMessage()
                    .contains("Argument long-option {opt} is used by multiple Arguments"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void test_arg_with_no_opt_should_throw() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument.builder().uniqueId("my named arg").build()
                    ),
                    new String[]{""}
            );
            fail("The above code should have thrown an exception.");
        } catch (ParseException e) {
            assertTrue(e.getMessage()
                    .contains("No short or long options provided to activate Argument: {my named arg}"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void test_arg_value_with_no_key_should_throw() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .shortOptions(Set.of('a'))
                                    .build()
                    ),
                    new String[]{"value"}
            );
            fail("The above code should have thrown an exception.");
        } catch (ParseException e) {
            assertTrue(e.getMessage()
                    .contains("Invalid/unknown CLI argument / value provided: {value}"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void test_short_args_are_recognised() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .builder()
                                .uniqueId("my named arg 1")
                                .shortOptions(Set.of('a'))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("my named arg 2")
                                .shortOptions(Set.of('b'))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("my named arg 3")
                                .shortOptions(Set.of('c'))
                                .build()
                ),
                new String[]{"-b"}
        );
        assertEquals(1, actualArgs.size(), 0);
        assertNull(actualArgs.get("my named arg 1"));
        assertEquals("my named arg 2", actualArgs.get("my named arg 2").getUniqueId());
        assertNull(actualArgs.get("my named arg 3"));
    }

    @Test
    void test_long_args_are_recognised() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .builder()
                                .uniqueId("1")
                                .longOptions(Set.of("a"))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("2")
                                .longOptions(Set.of("b"))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("3")
                                .longOptions(Set.of("c"))
                                .build()
                ),
                new String[]{"--b"}
        );
        assertEquals(1, actualArgs.size(), 0);
        assertNull(actualArgs.get("1"));
        assertEquals("2", actualArgs.get("2").getUniqueId());
        assertNull(actualArgs.get("3"));
    }

    @Test
    void test_unknown_short_arg() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .shortOptions(Set.of('a'))
                                    .build()
                    ),
                    new String[]{"-b"}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Invalid/unknown short CLI argument: b", e.getMessage());
        }
    }

    @Test
    void test_unknown_short_arg_is_okay() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .shortOptions(Set.of('a'))
                                    .build()
                    ),
                    new String[]{"-b"},
                    true,
                    false
            );
        } catch (ParseException e) {
            fail("The previous method call should not have thrown an exception.", e);
        }
    }

    @Test
    void test_unknown_short_arg_2() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .shortOptions(Set.of('a'))
                                    .build()
                    ),
                    new String[]{"-b=123"}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Invalid/unknown short CLI argument: b", e.getMessage());
        }
    }

    @Test
    void test_unknown_short_arg_2_is_okay() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .shortOptions(Set.of('a'))
                                    .build()
                    ),
                    new String[]{"-b=123"},
                    true,
                    false
            );
        } catch (ParseException e) {
            fail("The previous method call should not have thrown an exception.", e);
        }
    }

    @Test
    void test_unknown_long_arg() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .longOptions(Set.of("a"))
                                    .build()
                    ),
                    new String[]{"--b"}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Invalid/unknown long CLI argument: b", e.getMessage());
        }
    }

    @Test
    void test_unknown_long_arg_is_okay() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .longOptions(Set.of("a"))
                                    .build()
                    ),
                    new String[]{"--b"},
                    true,
                    false
            );
        } catch (ParseException e) {
            fail("The previous method call should not have thrown an exception.", e);
        }
    }

    @Test
    void test_short_arg_indicator_alone_throws() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .shortOptions(Set.of('a'))
                                    .build()
                    ),
                    new String[]{"-        "}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Short argument indicator found, but no argument provided.", e.getMessage());
        }
    }

    @Test
    void test_long_arg_indicator_alone_throws() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg")
                                    .longOptions(Set.of("a"))
                                    .build()
                    ),
                    new String[]{"--      "}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Long argument indicator found, but no argument provided.", e.getMessage());
        }
    }

    @Test
    void test_getting_boiler_plate_before_set_throws() {
        try {
            cliArgParser.getBoilerplate(Collections.emptyList());
            fail("The previous method call should have thrown an exception");
        } catch (ParseException e) {
            assertEquals("Boiler plate not yet set.", e.getMessage());
        }
    }

    @Test
    void test_getting_boiler_plate_after_set_equals() throws ParseException {
        cliArgParser.setBoilerplate("my name", "my usage syntax", "my synopsis", "my author", "where bugs");

        String actualBoilerplate = cliArgParser.getBoilerplate(List.of(
                Argument
                        .builder()
                        .uniqueId("my named arg 1")
                        .shortOptions(new TreeSet<>(Set.of('a', 'z')))
                        .longOptions(new TreeSet<>(Set.of("arg", "zulu")))
                        .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                "Suspendisse id odio a purus feugiat condimentum. Aliquam consectetur felis " +
                                "vehicula cursus consequat. Sed tincidunt, ligula sit amet faucibus tempus, " +
                                "felis augue lobortis nulla, et molestie risus eros in lorem. Donec ornare " +
                                "velit in condimentum finibus. In scelerisque ornare massa nec fermentum. " +
                                "Pellentesque semper felis non erat dignissim, vel porta risus maximus. Duis " +
                                "lobortis libero faucibus odio varius, quis rutrum leo posuere. Maecenas sed " +
                                "accumsan tortor, sed vehicula nisi. Phasellus ultrices tempus ullamcorper.")
                        .hasValue(true)
                        .defaultValue(123L)
                        .build(),
                Argument
                        .builder()
                        .uniqueId("my named arg 2")
                        .longOptions(new TreeSet<>(Set.of("clobber", "zap", "pow")))
                        .description("dont ask")
                        .build(),
                Argument
                        .builder()
                        .uniqueId("my named arg 3")
                        .shortOptions(new TreeSet<>(Set.of('y', 'o')))
                        .description("zoom zoom zoom zoom")
                        .hasValue(true)
                        .valueIsOptional(true)
                        .build()
        ));

        assertEquals("""

                NAME:\s
                    my name

                COMMAND:\s
                    my usage syntax

                SYNOPSIS:\s
                    my synopsis

                OPTIONS:\s
                    -a, -z, --arg, --zulu    =<value>    (default: 123)
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse id odio a purus feugiat condimentum. Aliquam consectetur felis vehicula cursus consequat. Sed tincidunt, ligula sit amet faucibus tempus, felis augue lobortis nulla, et molestie risus eros in lorem. Donec ornare velit in condimentum finibus. In scelerisque ornare massa nec fermentum. Pellentesque semper felis non erat dignissim, vel porta risus maximus. Duis lobortis libero faucibus odio varius, quis rutrum leo posuere. Maecenas sed accumsan tortor, sed vehicula nisi. Phasellus ultrices tempus ullamcorper.

                    --clobber, --pow, --zap
                        dont ask

                    -o, -y    (=<value>)
                        zoom zoom zoom zoom

                AUTHOR:\s
                    my author

                REPORTING BUGS:\s
                    where bugs

                """, actualBoilerplate);
    }

    @Test
    void test_mandatory_opts_are_collected_separately_in_boilerplate() throws ParseException {
        cliArgParser.setBoilerplate("my name", "my usage syntax", "my synopsis", "my author", "where bugs");

        String actualBoilerplate = cliArgParser.getBoilerplate(List.of(
                Argument
                        .builder()
                        .uniqueId("my named arg 1")
                        .shortOptions(new TreeSet<>(Set.of('a', 'z')))
                        .longOptions(new TreeSet<>(Set.of("arg", "zulu")))
                        .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                "Suspendisse id odio a purus feugiat condimentum. Aliquam consectetur felis ")
                        .hasValue(true)
                        .defaultValue(123L)
                        .build(),
                Argument
                        .builder()
                        .uniqueId("my named arg 2")
                        .longOptions(new TreeSet<>(Set.of("clobber", "zap", "pow")))
                        .description("dont ask")
                        .isMandatory(true)
                        .build(),
                Argument
                        .builder()
                        .uniqueId("my named arg 3")
                        .shortOptions(new TreeSet<>(Set.of('y', 'o')))
                        .description("zoom zoom zoom zoom")
                        .hasValue(true)
                        .valueIsOptional(true)
                        .build()
        ));

        assertEquals("""

                NAME:\s
                    my name

                COMMAND:\s
                    my usage syntax

                SYNOPSIS:\s
                    my synopsis

                MANDATORY OPTIONS:\s
                    --clobber, --pow, --zap
                        dont ask

                OPTIONS:\s
                    -a, -z, --arg, --zulu    =<value>    (default: 123)
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse id odio a purus feugiat condimentum. Aliquam consectetur felis\s

                    -o, -y    (=<value>)
                        zoom zoom zoom zoom

                AUTHOR:\s
                    my author

                REPORTING BUGS:\s
                    where bugs

                """, actualBoilerplate);
    }

    @Test
    void test_only_mandatory_opts_in_boilerplate() throws ParseException {
        cliArgParser.setBoilerplate("my name", "my usage syntax", "my synopsis", "my author", "where bugs");

        String actualBoilerplate = cliArgParser.getBoilerplate(List.of(
                Argument
                        .builder()
                        .uniqueId("my named arg 1")
                        .shortOptions(new TreeSet<>(Set.of('a', 'z')))
                        .longOptions(new TreeSet<>(Set.of("arg", "zulu")))
                        .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                "Suspendisse id odio a purus feugiat condimentum. Aliquam consectetur felis ")
                        .hasValue(true)
                        .defaultValue(123L)
                        .isMandatory(true)
                        .build(),
                Argument
                        .builder()
                        .uniqueId("my named arg 2")
                        .longOptions(new TreeSet<>(Set.of("clobber", "zap", "pow")))
                        .description("dont ask")
                        .isMandatory(true)
                        .build()
        ));

        assertEquals("""

                NAME:\s
                    my name

                COMMAND:\s
                    my usage syntax

                SYNOPSIS:\s
                    my synopsis

                MANDATORY OPTIONS:\s
                    -a, -z, --arg, --zulu    =<value>    (default: 123)
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse id odio a purus feugiat condimentum. Aliquam consectetur felis\s

                    --clobber, --pow, --zap
                        dont ask

                AUTHOR:\s
                    my author

                REPORTING BUGS:\s
                    where bugs

                """, actualBoilerplate);
    }

    @Test
    void test_mandatory_short_option_in_arg() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg")
                                .shortOptions(Set.of('a'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                ),
                new String[]{"-a=123"}
        );
        assertEquals(1, actualArgs.size(), 0);
        assertEquals(123, actualArgs.get("my named arg").getArgumentResult());
    }

    @Test
    void test_mandatory_short_option_is_pulled_from_cli() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg")
                                .shortOptions(Set.of('a'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                ),
                new String[]{"-a", "345"}
        );
        assertEquals(1, actualArgs.size(), 0);
        assertEquals(345, actualArgs.get("my named arg").getArgumentResult());
    }

    @Test
    void test_optional_short_option_only_comes_from_in_arg() throws ParseException {
        List<Argument<?>> input = List.of(
                Argument
                        .<Integer>builder()
                        .uniqueId("my named arg")
                        .shortOptions(Set.of('a'))
                        .hasValue(true)
                        .valueIsOptional(true)
                        .conversionFunction(Integer::parseInt)
                        .build()
        );

        // In arg passes;
        assertEquals(123,
                cliArgParser.parseFromCLI(input, new String[]{"-a=123"})
                        .get("my named arg").getArgumentResult()
        );


        // pull from next arg fails
        try {
            cliArgParser.parseFromCLI(input, new String[]{"-a", "123"});
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Invalid/unknown CLI argument / value provided: {123}", e.getMessage());
        }
    }


    @Test
    void test_mandatory_long_option_in_arg() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg")
                                .longOptions(Set.of("a"))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                ),
                new String[]{"--a=123"}
        );
        assertEquals(1, actualArgs.size(), 0);
        assertEquals(123, actualArgs.get("my named arg").getArgumentResult());
    }

    @Test
    void test_mandatory_long_option_is_pulled_from_cli() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg")
                                .longOptions(Set.of("a"))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                ),
                new String[]{"--a", "345"}
        );
        assertEquals(1, actualArgs.size(), 0);
        assertEquals(345, actualArgs.get("my named arg").getArgumentResult());
    }

    @Test
    void test_optional_long_option_only_comes_from_in_arg() throws ParseException {
        List<Argument<?>> input = List.of(
                Argument
                        .<Integer>builder()
                        .uniqueId("my named arg")
                        .longOptions(Set.of("a"))
                        .hasValue(true)
                        .valueIsOptional(true)
                        .conversionFunction(Integer::parseInt)
                        .build()
        );

        // In arg passes;
        assertEquals(123,
                cliArgParser.parseFromCLI(input, new String[]{"--a=123"})
                        .get("my named arg").getArgumentResult()
        );


        // pull from next arg fails
        try {
            cliArgParser.parseFromCLI(input, new String[]{"--a", "123"});
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Invalid/unknown CLI argument / value provided: {123}", e.getMessage());
        }
    }

    @Test
    void test_missing_mandatory_long_option() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .<Integer>builder()
                                    .uniqueId("my named arg")
                                    .longOptions(Set.of("a"))
                                    .hasValue(true)
                                    .valueIsOptional(false)
                                    .conversionFunction(Integer::parseInt)
                                    .build()
                    ),
                    new String[]{"--a"}
            );
            fail("The previous method call should have thrown an exception");
        } catch (ParseException e) {
            assertEquals("Missing mandatory value for long option: a", e.getMessage());
        }
    }

    @Test
    void test_missing_mandatory_short_option() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .<Integer>builder()
                                    .uniqueId("my named arg")
                                    .shortOptions(Set.of('a'))
                                    .hasValue(true)
                                    .valueIsOptional(false)
                                    .conversionFunction(Integer::parseInt)
                                    .build()
                    ),
                    new String[]{"-a"}
            );
            fail("The previous method call should have thrown an exception");
        } catch (ParseException e) {
            assertEquals("Missing mandatory value for short option: a", e.getMessage());
        }
    }

    @Test
    void test_missing_mandatory_argument() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .<Integer>builder()
                                    .uniqueId("1")
                                    .description("testing when argument is mandatory but not provided")
                                    .shortOptions(Set.of('a'))
                                    .isMandatory(true)
                                    .conversionFunction(Integer::parseInt)
                                    .build()
                    ),
                    new String[]{""}
            );
            fail("The previous method call should have thrown an exception");
        } catch (ParseException e) {
            assertEquals(
                    "Mandatory Argument was not provided {1 : testing when argument is mandatory but not provided}",
                    e.getMessage()
            );
        }
    }

    @Test
    void test_value_found_for_short_option_where_not_expected() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .<Integer>builder()
                                    .uniqueId("1")
                                    .shortOptions(Set.of('a'))
                                    .hasValue(false)
                                    .build()
                    ),
                    new String[]{"-a=123"}
            );
            fail("The previous method call should have thrown an exception");
        } catch (ParseException e) {
            assertEquals("Value found for short option where no value is expected: a=123", e.getMessage());
        }
    }

    @Test
    void test_value_found_for_long_option_where_not_expected() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .<Integer>builder()
                                    .uniqueId("my named arg")
                                    .longOptions(Set.of("a"))
                                    .hasValue(false)
                                    .build()
                    ),
                    new String[]{"--a=123"}
            );
            fail("The previous method call should have thrown an exception");
        } catch (ParseException e) {
            assertEquals("Value found for long option where no value is expected: a=123", e.getMessage());
        }
    }

    @Test
    void test_multiple_short_args_recognised_in_one_key() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .builder()
                                .uniqueId("1")
                                .shortOptions(Set.of('a'))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("2")
                                .shortOptions(Set.of('b'))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("3")
                                .shortOptions(Set.of('c'))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("4")
                                .shortOptions(Set.of('d'))
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("5")
                                .shortOptions(Set.of('e'))
                                .build()
                ),
                new String[]{"-ace"}
        );
        assertEquals(3, actualArgs.size(), 0);
        assertEquals("1", actualArgs.get("1").getUniqueId());
        assertNull(actualArgs.get("2"));
        assertEquals("3", actualArgs.get("3").getUniqueId());
        assertNull(actualArgs.get("4"));
        assertEquals("5", actualArgs.get("5").getUniqueId());
    }

    @Test
    void test_multiple_short_args_with_values_in_one_key_throws() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("my named arg 1")
                                    .shortOptions(Set.of('a'))
                                    .build()
                            , Argument
                                    .builder()
                                    .uniqueId("my named arg 2")
                                    .shortOptions(Set.of('b'))
                                    .build()
                    ),
                    new String[]{"-ab=123"}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Multiple short arguments provided in conjunction with an argument value. " +
                    "If short argument requires a value, provide the arg separately.", e.getMessage());
        }
    }

    @Test
    void test_multiple_short_args_with_values_consumer_later_in_cli() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .builder()
                                .uniqueId("1")
                                .shortOptions(Set.of('a'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("2")
                                .shortOptions(Set.of('b'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("3")
                                .shortOptions(Set.of('c'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("4")
                                .shortOptions(Set.of('d'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                        , Argument
                                .builder()
                                .uniqueId("5")
                                .shortOptions(Set.of('e'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                ),
                new String[]{"-ace", "1", "2", "3"}
        );
        assertEquals(3, actualArgs.size(), 0);
        assertEquals("1", actualArgs.get("1").getUniqueId());
        assertEquals(1, actualArgs.get("1").getArgumentResult());
        assertEquals("3", actualArgs.get("3").getUniqueId());
        assertEquals(2, actualArgs.get("3").getArgumentResult());
        assertEquals("5", actualArgs.get("5").getUniqueId());
        assertEquals(3, actualArgs.get("5").getArgumentResult()
        );
    }


    @Test
    void test_validation_of_arg_value_input_with_failure() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .<Integer>builder()
                                    .uniqueId("1")
                                    .shortOptions(Set.of('a'))
                                    .hasValue(true)
                                    .conversionFunction(Integer::parseInt)
                                    .validationFunction(in -> in > 10 && in < 20)
                                    .description("Used as some arg somewhere. Arg value should be > 10 and < 20")
                                    .build()
                    ),
                    new String[]{"-a=123"}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Argument value failed validation. Check argument {1} documentation: {Used as some arg somewhere. Arg value should be > 10 and < 20}", e.getMessage());
        }
    }

    @Test
    void test_validation_of_arg_value_input_with_success() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Double>builder()
                                .uniqueId("my named arg")
                                .shortOptions(Set.of('a'))
                                .hasValue(true)
                                .conversionFunction(Double::parseDouble)
                                .validationFunction(in -> in.equals(3.1415D))
                                .description("Used as some arg somewhere. Arg value should be pi")
                                .build()
                ),
                new String[]{"-a=3.1415"}
        );

        assertEquals(1, actualArgs.size(), 0);
        assertEquals("my named arg", actualArgs.get("my named arg").getUniqueId());
        assertEquals(3.1415, actualArgs.get("my named arg").getArgumentResult());
    }


    @Test
    void test_give_back_args_with_default_values_when_not_provided() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 1")
                                .shortOptions(Set.of('a'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 2")
                                .shortOptions(Set.of('b'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .defaultValue(565)
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 3")
                                .shortOptions(Set.of('c'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build()
                ),
                new String[]{"-a", "34"},
                false,
                true
        );


        assertEquals(2, actualArgs.size(), 0);
        assertEquals(34, actualArgs.get("my named arg 1").getArgumentResult());
        assertNull(actualArgs.get("my named arg 3"));
        assertEquals(565, actualArgs.get("my named arg 2").getArgumentResult());
    }

    @Test
    void test_give_back_args_with_default_values_wont_override_when_provided() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 1")
                                .shortOptions(Set.of('a'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 2")
                                .shortOptions(Set.of('b'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .defaultValue(200)
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 3")
                                .shortOptions(Set.of('c'))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .defaultValue(300)
                                .build()
                ),
                new String[]{"-a", "100", "-b", "941"},
                false,
                true
        );


        assertEquals(3, actualArgs.size(), 0);
        assertEquals(100, actualArgs.get("my named arg 1").getArgumentResult());
        assertEquals(941, actualArgs.get("my named arg 2").getArgumentResult());
        assertEquals(300, actualArgs.get("my named arg 3").getArgumentResult());
    }

    @Test
    void test_unique_ids_must_be_unique() {
        try {
            cliArgParser.parseFromCLI(
                    List.of(
                            Argument
                                    .builder()
                                    .uniqueId("must be unique")
                                    .shortOptions(Set.of('a'))
                                    .build()
                            , Argument
                                    .builder()
                                    .uniqueId("MuSt Be uNiQue")
                                    .shortOptions(Set.of('b'))
                                    .build()
                    ),
                    new String[]{"-ab=123"}
            );
            fail("The previous method call should have thrown an exception.");
        } catch (ParseException e) {
            assertEquals("Either, provided more than one CLI Argument with the same ID, " +
                    "these must be unique; or no ID was provided.", e.getMessage());
        }
    }

    @Test
    void test_short_circuit_args_are_not_interrupted_by_missing_mandatory_args() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 1")
                                .shortOptions(Set.of('a'))
                                .shouldShortCircuit(true)
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 2")
                                .shortOptions(Set.of('b'))
                                .isMandatory(true)
                                .build()
                ),
                new String[]{"-a"}
        );

        assertEquals(1, actualArgs.size(), 0);
        assertTrue(actualArgs.containsKey("my named arg 1"));
    }

    @Test
    void test_short_circuit_args_cut_out_other_provided_args_except_other_short_circuits() throws ParseException {
        Map<String, Argument<Object>> actualArgs = cliArgParser.parseFromCLI(
                List.of(
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 1")
                                .shortOptions(Set.of('a'))
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 2")
                                .shortOptions(Set.of('b'))
                                .shouldShortCircuit(true)
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 3")
                                .shortOptions(Set.of('c'))
                                .isMandatory(true)
                                .build(),
                        Argument
                                .<Integer>builder()
                                .uniqueId("my named arg 4")
                                .shortOptions(Set.of('d'))
                                .shouldShortCircuit(true)
                                .build()
                ),
                new String[]{"-a", "-b", "-d"}
        );

        assertEquals(2, actualArgs.size(), 0);
        assertTrue(actualArgs.containsKey("my named arg 2"));
        assertTrue(actualArgs.containsKey("my named arg 4"));
    }

}
