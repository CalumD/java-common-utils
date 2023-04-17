package com.clumd.projects.java_common_utils.arg_parser;

import lombok.NonNull;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A useful implementation of {@link CLIArgParser} with all basic features supported.
 */
public class JavaArgParser implements CLIArgParser {

    private static final String INDENT = "    ";
    private static final String NEWLINE = "\n";
    private static final String SPACE_LINE = NEWLINE + NEWLINE;
    private String appName;
    private String appSyntax;
    private String appSynopsis;
    private String appAuthor;
    private String appBugs;

    private Collection<Argument<?>> possibleArguments;
    private Map<String, Argument<Object>> returnArgumentMap;
    private boolean ignoreUnknownCLIArgs;

    @Override
    public Map<String, Argument<Object>> parseFromCLI(
            @NonNull Collection<Argument<?>> possibleArguments,
            @NonNull String[] args
    ) throws ParseException {
        return parseFromCLI(possibleArguments, args, false, false);
    }

    @Override
    public synchronized Map<String, Argument<Object>> parseFromCLI(
            @NonNull Collection<Argument<?>> possibleArguments,
            @NonNull String[] args,
            boolean ignoreUnknownCLIArgs,
            boolean returnArgsWithDefaultButNotOnCLI
    ) throws ParseException {
        // We use a map here as the collection type, to ensure that if there are duplicate options provided on the CLI,
        // that only one (the closest to the end) will become impactful.
        returnArgumentMap = new LinkedHashMap<>();

        // Set references
        this.possibleArguments = possibleArguments;
        this.ignoreUnknownCLIArgs = ignoreUnknownCLIArgs;

        // First, check that all the arguments provided by the user are acceptable to this logic
        sanitise();

        // Now, populate a map of all the options
        Map<Character, Argument<?>> shortArgMap = new HashMap<>(possibleArguments.size());
        Map<String, Argument<?>> longArgMap = new HashMap<>(possibleArguments.size());

        possibleArguments.forEach(arg -> {
            arg.getShortOptions().forEach(shortArg -> shortArgMap.put(shortArg, arg));
            arg.getLongOptions().forEach(longArg -> longArgMap.put(longArg, arg));
        });

        // Carry out the actual argument parsing:
        Iterator<String> argumentIterator = Arrays.stream(args).iterator();
        String currentWholeCLI;

        while (argumentIterator.hasNext()) {
            currentWholeCLI = argumentIterator.next();

            if (currentWholeCLI.startsWith("--")) {
                currentWholeCLI = currentWholeCLI.substring(2);

                if (currentWholeCLI.strip().isBlank()) {
                    throw new ParseException("Long argument indicator found, but no argument provided.", 0);
                }

                String longArgKey = currentWholeCLI.contains("=")
                        ? currentWholeCLI.split("=")[0]
                        : currentWholeCLI;

                parseLongArgument(
                        currentWholeCLI,
                        longArgKey,
                        longArgMap.getOrDefault(longArgKey, null),
                        argumentIterator
                );

            } else if (currentWholeCLI.startsWith("-")) {
                currentWholeCLI = currentWholeCLI.substring(1);

                if (currentWholeCLI.strip().isBlank()) {
                    throw new ParseException("Short argument indicator found, but no argument provided.", 0);
                }

                if (currentWholeCLI.contains("=")) {
                    parseSingleShortArgWithValue(currentWholeCLI, shortArgMap);
                } else {
                    parseMultipleShortArgsFromCLIEntry(currentWholeCLI, argumentIterator, shortArgMap);
                }

            } else {
                if (!currentWholeCLI.strip().isBlank() && !ignoreUnknownCLIArgs) {
                    throw new ParseException("Invalid/unknown CLI argument / value provided: {" + currentWholeCLI + "}", 0);
                }
            }
        }

        // Check for any short circuit-able args
        boolean thereAreShortCircuits = false;
        for (Argument<?> arg : possibleArguments) {
            if (arg.shouldShortCircuit() && returnArgumentMap.get(arg.getUniqueId()) != null) {
                thereAreShortCircuits = true;
                break;
            }
        }
        if (thereAreShortCircuits) {
            return returnArgumentMap
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().shouldShortCircuit())
                    .collect(Collectors.toUnmodifiableMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));
        }

        // Double check that any mandatory arguments were provided
        for (Argument<?> arg : possibleArguments) {
            if (arg.isMandatory() && (returnArgumentMap.get(arg.getUniqueId()) == null)) {
                throw new ParseException("Mandatory Argument was not provided {" + arg.getUniqueId() + " : " + arg.getDescription() + "}", 0);
            }
        }

        // Optionally return Arguments which were not provided, but which have default values set by the caller
        if (returnArgsWithDefaultButNotOnCLI) {
            for (Argument<?> arg : possibleArguments) {
                if (returnArgumentMap.get(arg.getUniqueId()) == null && arg.isDefaultValueSet()) {
                    genericParseIntoArgument(arg, null);
                }
            }
        }

        return returnArgumentMap;
    }

    private void sanitise() throws ParseException {
        if (possibleArguments.isEmpty()) {
            throw new ParseException("No arguments provided to parse for.", 0);
        }
        // Ensure we have no duplicate argument IDs
        final int distinctArgInputCount = possibleArguments
                .stream()
                .map(Argument::getUniqueId)
                .map(String::toUpperCase)
                .filter(uniqueId -> !"_._._._._._._._._._.".equals(uniqueId))
                .distinct()
                .toList()
                .size();
        if (distinctArgInputCount != possibleArguments.size()) {
            throw new ParseException("Either, provided more than one CLI Argument with the same ID, " +
                    "these must be unique; or no ID was provided.", 0);
        }

        // Ensure that no two arguments have the same short args or long args
        Set<Character> allShortArgs = new HashSet<>();
        Set<String> allLongArgs = new HashSet<>();
        for (Argument<?> arg : possibleArguments) {
            if (arg.getShortOptions().isEmpty() && arg.getLongOptions().isEmpty()) {
                throw new ParseException("No short or long options provided to activate Argument: {"
                        + arg.getUniqueId() + "}", 0);
            }
            for (Character c : arg.getShortOptions()) {
                if (allShortArgs.contains(c)) {
                    throw new ParseException("Argument short-option {" + c + "} is used by multiple Arguments", 0);
                }
                allShortArgs.add(c);
            }
            for (String s : arg.getLongOptions()) {
                if (allLongArgs.contains(s)) {
                    throw new ParseException("Argument long-option {" + s + "} is used by multiple Arguments", 0);
                }
                allLongArgs.add(s);
            }
        }
    }

    private void parseLongArgument(String currentWholeCLI, String longArgKey, Argument<?> currentArg, Iterator<String> argumentIterator) throws ParseException {
        if (currentArg == null) {
            if (ignoreUnknownCLIArgs) {
                return;
            }
            throw new ParseException("Invalid/unknown long CLI argument: " + longArgKey, 0);
        }

        // Check if the argument requires a value
        if (currentArg.hasValue() && !currentArg.valueIsOptional()) {

            // Decide if we should pass in the next CLI entity as a value to this long arg
            if (currentWholeCLI.contains("=")) {
                genericParseIntoArgument(
                        currentArg,
                        currentWholeCLI.substring(currentWholeCLI.indexOf("=") + 1));
            } else {
                if (argumentIterator.hasNext()) {
                    genericParseIntoArgument(currentArg, argumentIterator.next());
                } else {
                    throw new ParseException("Missing mandatory value for long option: " + longArgKey, 0);
                }
            }

        } else if (currentArg.hasValue()) {

            genericParseIntoArgument(
                    currentArg,
                    currentWholeCLI.contains("=")
                            ? currentWholeCLI.substring(currentWholeCLI.indexOf("=") + 1)
                            : null
            );

        } else if (currentWholeCLI.contains("=")) {
            throw new ParseException("Value found for long option where no value is expected: " + currentWholeCLI, 0);
        } else {
            genericParseIntoArgument(currentArg, null);
        }
    }

    private void parseSingleShortArgWithValue(String currentWholeCLI, Map<Character, Argument<?>> shortArgMap) throws ParseException {
        Argument<?> currentArg;
        String shortArgKey = currentWholeCLI.split("=")[0];
        if (shortArgKey.length() != 1) {
            throw new ParseException("Multiple short arguments provided in conjunction with an argument value. " +
                    "If short argument requires a value, provide the arg separately.", 0);
        } else {
            currentArg = shortArgMap.getOrDefault(shortArgKey.charAt(0), null);
        }

        if (currentArg == null) {
            if (ignoreUnknownCLIArgs) {
                return;
            }
            throw new ParseException("Invalid/unknown short CLI argument: " + shortArgKey, 0);
        }
        if (!currentArg.hasValue()) {
            throw new ParseException("Value found for short option where no value is expected: " + currentWholeCLI, 0);
        }

        genericParseIntoArgument(
                currentArg,
                currentWholeCLI.substring(currentWholeCLI.indexOf("=") + 1)
        );
    }

    private void parseMultipleShortArgsFromCLIEntry(String currentWholeCLI, Iterator<String> argumentIterator, Map<Character, Argument<?>> shortArgMap) throws ParseException {
        Argument<?> currentArg;
        for (Character shortArgKey : currentWholeCLI.toCharArray()) {

            currentArg = shortArgMap.getOrDefault(shortArgKey, null);
            if (currentArg == null) {
                if (ignoreUnknownCLIArgs) {
                    return;
                }
                throw new ParseException("Invalid/unknown short CLI argument: " + shortArgKey, 0);
            }

            if (currentArg.hasValue() && !currentArg.valueIsOptional()) {

                // Decide if we should pass in the next CLI entity as a value to this short arg
                if (argumentIterator.hasNext()) {
                    genericParseIntoArgument(currentArg, argumentIterator.next());
                } else {
                    throw new ParseException("Missing mandatory value for short option: " + shortArgKey, 0);
                }

            } else {
                genericParseIntoArgument(currentArg, null);
            }
        }
    }

    @SuppressWarnings("unchecked") // Every Argument<?> WILL be an Argument of type <Object>, so no concern here.
    private void genericParseIntoArgument(final Argument<?> argDef, final String value) throws ParseException, IllegalArgumentException {

        argDef.attemptValueConversion(value);

        if (!argDef.validateValue()) {
            throw new ParseException("Argument value failed validation. Check argument {" + argDef.getUniqueId() + "} documentation: {" + argDef.getDescription() + "}", 0);
        }

        returnArgumentMap.put(argDef.getUniqueId(), (Argument<Object>) argDef);
    }

    @Override
    public void setBoilerplate(
            @NonNull final String name,
            @NonNull final String usageSyntax,
            @NonNull final String synopsis,
            @NonNull final String author,
            @NonNull final String bugs
    ) {
        this.appName = name;
        this.appSyntax = usageSyntax;
        this.appSynopsis = synopsis;
        this.appAuthor = author;
        this.appBugs = bugs;
    }

    @Override
    public String getBoilerplate(@NonNull final Collection<Argument<?>> forArguments) throws ParseException {
        if (appName == null) {
            throw new ParseException("Boiler plate not yet set.", 0);
        }

        StringBuilder mandatoryOpts = new StringBuilder();
        StringBuilder optionalOpts = new StringBuilder();

        forArguments.forEach(arg -> {
            if (arg.isMandatory()) {
                compileKnownOptsForBoilerplate(mandatoryOpts, arg);
            } else {
                compileKnownOptsForBoilerplate(optionalOpts, arg);
            }
        });

        return NEWLINE +
                "NAME: " + NEWLINE + INDENT + appName + SPACE_LINE +
                "COMMAND: " + NEWLINE + INDENT + appSyntax + SPACE_LINE +
                "SYNOPSIS: " + NEWLINE + INDENT + appSynopsis + SPACE_LINE +
                (mandatoryOpts.length() > 0 ? ("MANDATORY OPTIONS: " + NEWLINE + mandatoryOpts) : "") +
                (optionalOpts.length() > 0 ? ("OPTIONS: " + NEWLINE + optionalOpts) : "") +
                "AUTHOR: " + NEWLINE + INDENT + appAuthor + SPACE_LINE +
                "REPORTING BUGS: " + NEWLINE + INDENT + appBugs + SPACE_LINE;
    }

    private void compileKnownOptsForBoilerplate(StringBuilder mandatoryOpts, Argument<?> arg) {
        mandatoryOpts.append(INDENT);
        arg.getShortOptions().forEach(shortArg -> mandatoryOpts.append('-').append(shortArg).append(", "));
        arg.getLongOptions().forEach(longArg -> mandatoryOpts.append("--").append(longArg).append(", "));
        mandatoryOpts.delete(mandatoryOpts.length() - 2, mandatoryOpts.length());
        if (arg.hasValue()) {
            mandatoryOpts.append(INDENT);
            if (arg.valueIsOptional()) {
                mandatoryOpts.append("(=<value>)");
            } else {
                mandatoryOpts.append("=<value>");
            }
            if (arg.isDefaultValueSet()) {
                mandatoryOpts.append(INDENT).append("(default: ").append(arg.getDefaultValue()).append(")");
            }
        }

        mandatoryOpts.append(NEWLINE).append(INDENT).append(INDENT);
        mandatoryOpts.append(arg.getDescription());
        mandatoryOpts.append(SPACE_LINE);
    }
}
