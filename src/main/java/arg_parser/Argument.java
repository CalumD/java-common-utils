package arg_parser;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * This Class represents all the configuration required to parse a single Command Line Argument into a usable output
 */
@Data
@Builder
public class Argument<T> {

    /**
     * Used to index the options
     */
    private final int uniqueId;

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
     * Used to indicate if this argument can be followed by a value, defaults to false
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final boolean hasValue = false;

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
     * If this argument needs a value, then this should be the fully validated and parsed output of that value.
     */
    private T argumentResult;


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
     * This method should be called by the implementing {@link CLIArgParser} if a value is provided for this Argument.
     *
     * @param value The CLI Argument's value to be parsed into the desired Java type, or a null if no argument provided
     */
    public final void attemptValueConversion(final String value) {
        if (value != null) {
            argumentResult = conversionFunction.apply(value);
        }
    }
}