package com.clumd.projects.java_common_utils.arg_parser;

import com.clumd.projects.java_common_utils.base_enhancements.FunctionPotentialException;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * This Class represents all the configuration required to parse a single Command Line Argument into a usable output
 * <p></p>
 * The Ideal implementation pattern for this is as follows:
 * <pre>
 * {@code
 * public class YourProgramsArgParser {
 *
 *      private static final List<Argument<?>> arguments;
 *      private static final Argument<Void> helpFlag, otherHelpStyleFlags;
 *      private static final Argument<String> someArgumentWithATypedValue;
 *
 *      static {
 *          helpFlag = Argument.<Void>builder()
 *                 .uniqueId("help flag")
 *                 .shouldShortCircuit(true)
 *                 ...
 *                 .build();
 *          otherHelpStyleFlags = Argument.<Void>builder()
 *                 .uniqueId("other flag")
 *                 ...
 *                 .build();
 *          someArgumentWithATypedValue = Argument.<String>builder()
 *                 .uniqueId("typed arg")
 *                 ...
 *                 .build();
 *
 *          arguments = List.of(helpFlag,
 *                 otherHelpStyleFlags,
 *                 someArgumentWithATypedValue);
 *
 *         argParser = new JavaArgParser();
 *         argParser.setBoilerplate(...);
 *      }
 *
 *      public static ObjectToActionArgsWith getArgs(String[] args) throws UnwrappableThrowable {
 *
 *          ObjectToActionArgsWith objectToActionArgsWith = new ObjectToActionArgsWith;
 *
 *          Map<String, Argument<Object>> parsedArguments;
 *          parsedArguments = argParser.parseFromCLI(arguments, args);
 *
 *          for (String argID : parsedArguments.keySet()) {
 *              switch (argID) {
 *                  case "help flag" -> {
 *                      System.out.println(argParser.getBoilerplate(arguments));
 *                      throw new UnwrappableRuntimeException("Instant quit.");
 *                  }
 *                  case "other flag" -> otherHelpStyleFlags.getArgumentResult();
 *                  case "typed arg" -> someArgumentWithATypedValue.getArgumentResult();
 *                  default ->
 *                      throw new UnwrappableException("Argument {" + argID + "} configured, but not handled when present.");
 *              }
 *          }
 *
 *          return objectToActionArgsWith;
 *      }
 * }
 * }
 * </pre>
 *
 * @param <T> The type this Argument's value represents be post parsing.
 */
@Data
@Builder
public class Argument<T> {

    /**
     * Used to index the options, must be unique and supplied by the user.
     */
    private final String uniqueId;

    /**
     * Used to contain all the single character alias' to indicate this option
     */
    @Builder.Default
    private final Set<Character> shortOptions = new HashSet<>();

    /**
     * Used to contain all the word alias' to indicate this option
     */
    @Builder.Default
    private final Set<String> longOptions = new HashSet<>();

    /**
     * Used to denote other CLI Arguments (by reference to their {@link Argument#uniqueId}) which MUST be present if,
     * and only if, this Argument is found on the CLI.
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final Set<String> mustBeUsedWith = new HashSet<>();

    /**
     * Used to denote other CLI Arguments (by reference to their {@link Argument#uniqueId}) which MUST NOT be used in
     * conjunction with this one if, and only if, this Argument is found on the CLI.
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final Set<String> mustNotBeUsedWith = new HashSet<>();

    /**
     * Used to indicate if this argument is mandatory and MUST be provided for the utilising code to function. If you
     * set this value to true, you probably don't want to set a default value.
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final boolean isMandatory = false;

    /**
     * Used to tell a parser that if this argument is detected on the CLI at all, it should ALWAYS be returnable to the
     * caller with all present short circuit args. Ideally ignoring any other constraints, such as
     * {@link Argument#isMandatory} = true; or an already parsed argument with {@link Argument#mustBeUsedWith} set.
     * This will only take effect, if a short-circuiting argument is found on the command line before another CLI
     * option which would cause the parser to fail.
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final boolean shouldShortCircuit = false;

    /**
     * Used to indicate if this argument can be followed by a value, defaults to false
     */
    @Accessors(fluent = true)
    private final boolean hasValue;

    /**
     * Used to indicate if the value to this argument can be optional, defaults to false. (e.g. value MUST be provided)
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final boolean valueIsOptional = false;

    /**
     * Used to describe the purpose of this Argument and how it should be used.
     */
    @Builder.Default
    private final String description = "";

    /**
     * This function can be provided to run a verification step that the argument's value is acceptable.
     */
    private final Function<T, Boolean> validationFunction;

    /**
     * This function is used to convert an argument from the CLI string form, into the object the running process
     * needs.
     */
    private final Function<String, T> conversionFunction;

    /**
     * Used to track if a default value has been set for this argument to be used if no CLI value is found.
     */
    private boolean defaultValueSet;

    /**
     * If this argument needs a value, then this should be the fully validated and parsed output of that value.
     */
    private T argumentResult;
    /**
     * This should be used as the default value, if nothing is provided for the conversion function
     */
    private T defaultValue;

    /**
     * This method should be called by the implementing {@link CLIArgParser} if a value is provided for this Argument.
     *
     * @param value The CLI Argument's value to be parsed into the desired Java type, or a null if no argument provided
     */
    public final void attemptValueConversion(final String value) {
        if (value != null) {
            argumentResult = conversionFunction.apply(value);
        } else {
            argumentResult = defaultValue;
        }
    }

    /**
     * If a value is possible for this CLI Argument, then this method will be called to verify that the argument
     * provided is acceptable by the calling process. This works by calling the implementation-provided
     * {@link #validationFunction} to decide if it is acceptable
     *
     * @return True (default if no function was provided), if the value provided meets the acceptance criteria of the
     * {@link #validationFunction}, False if it fails validation
     */
    public final boolean validateValue() {

        // If the validation function is not provided, we should default to accepted.
        if (validationFunction != null) {
            return validationFunction.apply(argumentResult);
        }

        return true;
    }

    /**
     * Overridden to make development cognitively easier by removing the Argument config, and only showing the output
     * for itself.
     *
     * @return The string representation of the Argument Result to make scanning in debuggers easier.
     */
    @Override
    public String toString() {
        return getArgumentResult() == null ? "*no value*" : getArgumentResult().toString();
    }

    /**
     * Overridden Builder for part of the Lombok process. This is used to alter the basic setter functionality of the
     * chained methods in the builder, dependent on the field being overridden.
     *
     * @param <T> The param type for the builder matching that of the base Argument class.
     */
    public static class ArgumentBuilder<T> {

        // This variable has to be brought into this builder to be used in potential
        // exception messages of conversion / validation functions
        String uniqueId = "_._._._._._._._._._.";
        boolean defaultValueSet = false;
        boolean hasValue = false;
        private boolean hasValueWasSetByDirectSetter = false;

        /**
         * This is used to set the uniqueId of this argument, it is only required as an override to the default
         * constructor to have the value available to the exception strings in the {@link FunctionPotentialException}
         * methods below
         *
         * @param toValue Used to index the options, must be unique and supplied by the user.
         * @return This continued builder instance.
         */
        public Argument.ArgumentBuilder<T> uniqueId(String toValue) {
            uniqueId = toValue;
            return this;
        }

        /**
         * This is used to set the hasValue of this argument, it is only required as an override to the default
         * constructor to allow the conversionFunction to trigger a change of state when a conversionFunction is
         * provided.
         *
         * @param toValue Used to determine whether this Argument can be followed with a value.
         * @return This continued builder instance.
         */
        public Argument.ArgumentBuilder<T> hasValue(boolean toValue) {
            hasValue = toValue;
            hasValueWasSetByDirectSetter = true;
            return this;
        }

        /**
         * Used to set the default value, and also mark that the default value has been set. This can be used to notify
         * a local Arg Parser that a local value exists, in case it wants to display this in help text.
         *
         * @param toValue The value to use as a default value for this argument.
         * @return This continued builder instance.
         */
        public Argument.ArgumentBuilder<T> defaultValue(T toValue) {
            defaultValue = toValue;
            defaultValueSet = true;
            return this;
        }

        /**
         * This constructor is used to override the conversion function in order to wrap the call to the
         * {@link Function#apply(Object)} such that the implementing user can use checked exceptions if they want,
         * without it causing compile time issues. Any exceptions will be wrapped into the reason for an
         * {@link IllegalArgumentException}.
         *
         * @param functionWhichMayThrowException An input lambda which may or may not throw an exception. The lambda's
         *                                       single input parameter will be a String which would be the command line
         *                                       argument value. The return type is tied to the typing of the base
         *                                       {@link Argument<T>}. The exception is introspected by the definition of
         *                                       the provided lambda.
         * @param <E>                            Allowing the {@link FunctionPotentialException} to throw an instance of
         *                                       an Exception, as a child reason to an IllegalArgumentException.
         * @return This continued builder instance.
         */
        public <E extends Exception> Argument.ArgumentBuilder<T> conversionFunction(FunctionPotentialException<String, T, E> functionWhichMayThrowException) {
            conversionFunction = cli -> {
                try {
                    return functionWhichMayThrowException.apply(cli);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Argument with ID {" + uniqueId + "} failed to parse from CLI arg. " +
                            "Check supplied value for typos or read the description for this value in the --help.", e);
                }
            };

            // If you provide a conversion function, then the Argument must be capable of accepting a value,
            // we shouldn't need to explicitly state that otherwise.
            // However, if the value was already set by the setter, we should NOT override it.
            if (!hasValueWasSetByDirectSetter) {
                hasValue = true;
            }
            return this;
        }

        /**
         * This constructor is used to override the value validation function in order to wrap the call to the
         * {@link Function#apply(Object)} such that the implementing user can use checked exceptions if they want,
         * without it causing compile time issues. Any exceptions will be wrapped into the reason for an
         * {@link IllegalArgumentException}.
         *
         * @param functionWhichMayThrowException An input lambda which may or may not throw an exception. The lambda's
         *                                       single input parameter will be the type-safe value, typed against the T
         *                                       of the base {@link Argument}. The return type will be a boolean
         *                                       determining whether the successfully parsed value is within some
         *                                       user-defined acceptable range. The exception is introspected by the
         *                                       definition of the provided lambda.
         * @param <E>                            Allowing the {@link FunctionPotentialException} to throw an instance of
         *                                       an Exception, as a child reason to an IllegalArgumentException.
         * @return This continued builder instance.
         */
        public <E extends Exception> Argument.ArgumentBuilder<T> validationFunction(FunctionPotentialException<T, Boolean, E> functionWhichMayThrowException) {
            validationFunction = cli -> {
                try {
                    Boolean valueValid = functionWhichMayThrowException.apply(cli);
                    // I can see there being some poor soul who has written a poor validationFunction implementation
                    // which nulls out and gets caught here, so adding this additional check to be safe.
                    // We are only unable to return a primitive in the method declaration due to Java's typed-parameters not allowing primitives.
                    if (valueValid == null) {
                        throw new ParseException("Validation function returned a null instead of true/false. " +
                                "Please check for edge cases.", 0);
                    }
                    return valueValid;
                } catch (Exception e) {
                    throw new IllegalArgumentException("Argument with ID {" + uniqueId + "} failed to validate. " +
                            "Check supplied value, or that the default value is valid for the given Validation function, " +
                            "if providing an argument is optional.", e);
                }
            };
            return this;
        }
    }
}
