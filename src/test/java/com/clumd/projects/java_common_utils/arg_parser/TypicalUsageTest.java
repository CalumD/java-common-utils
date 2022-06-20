package com.clumd.projects.java_common_utils.arg_parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TypicalUsageTest {

    private CLIArgParser argParser;
    private Map<Integer, Argument<?>> inputArgs;
    private List<Argument<?>> actualArgs;

    @BeforeEach
    void setUp() {
        argParser = new JavaArgParser();
        inputArgs = getInputArguments();
    }

    @Test
    void test_marker_argument_is_picked_up() throws ParseException {
        actualArgs = new ArrayList<>(argParser.parseFromCLI(inputArgs.values(), new String[]{"--alpha"}));
        assertEquals(1, actualArgs.size(), 0);
        assertNull(actualArgs.get(0).getArgumentResult());
    }

    @Test
    void test_duplicate_option_is_overridden_by_last() throws ParseException {
        actualArgs = new ArrayList<>(argParser.parseFromCLI(inputArgs.values(), new String[]{"-f=56", "-f=63", "-f=59"}));
        assertEquals(1, actualArgs.size(), 0);
        assertEquals(59, (Integer) actualArgs.get(0).getArgumentResult(), 0);
    }

    private Map<Integer, Argument<?>> getInputArguments() {
        return Stream.of(
                        Argument.
                                <Integer>builder()
                                .uniqueId(1)
                                .shortOptions(Set.of('a'))
                                .longOptions(Set.of("alpha"))
                                .hasValue(false)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build(),
                        Argument.
                                <Integer>builder()
                                .uniqueId(2)
                                .shortOptions(Set.of('b'))
                                .longOptions(Set.of("beta"))
                                .hasValue(false)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .defaultValue(20)
                                .build(),
                        Argument.
                                <Integer>builder()
                                .uniqueId(3)
                                .shortOptions(Set.of('c'))
                                .longOptions(Set.of("charlie"))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .build(),
                        Argument.
                                <Integer>builder()
                                .uniqueId(4)
                                .shortOptions(Set.of('d'))
                                .longOptions(Set.of("delta"))
                                .hasValue(true)
                                .valueIsOptional(false)
                                .conversionFunction(Integer::parseInt)
                                .validationFunction(f -> f > 35 && f < 45)
                                .build(),
                        Argument.
                                <Integer>builder()
                                .uniqueId(5)
                                .shortOptions(Set.of('e'))
                                .longOptions(Set.of("echo"))
                                .hasValue(true)
                                .valueIsOptional(true)
                                .conversionFunction(Integer::parseInt)
                                .build(),
                        Argument.
                                <Integer>builder()
                                .uniqueId(6)
                                .shortOptions(Set.of('f'))
                                .longOptions(Set.of("foxtrot"))
                                .hasValue(true)
                                .valueIsOptional(true)
                                .conversionFunction(Integer::parseInt)
                                .validationFunction(f -> f > 55 && f < 65)
                                .build(),
                        Argument.
                                <Integer>builder()
                                .uniqueId(7)
                                .shortOptions(Set.of('g'))
                                .longOptions(Set.of("golf"))
                                .hasValue(true)
                                .valueIsOptional(true)
                                .conversionFunction(Integer::parseInt)
                                .defaultValue(70)
                                .build(),
                        Argument.
                                <Integer>builder()
                                .uniqueId(8)
                                .shortOptions(Set.of('h'))
                                .longOptions(Set.of("hotel"))
                                .hasValue(true)
                                .valueIsOptional(true)
                                .conversionFunction(Integer::parseInt)
                                .validationFunction(f -> f > 75 && f < 85)
                                .defaultValue(80)
                                .build()
                )
                .collect(Collectors.toMap(
                        Argument::getUniqueId,
                        a -> a
                ));
    }
}
