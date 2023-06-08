package com.clumd.projects.java_common_utils.base_enhancements;

import java.io.Serializable;
import java.util.List;

/**
 * This base-level exception interface is used to add some additional methods to {@link Throwable}s which may prove
 * useful for compressing large stack-traces.
 * Rather than showing all the individual code line traces, we can just print the tree of exception names and TOP-level
 * messages; allowing for a more digestible output.
 * But optionally still provide the ability to ask for it all.
 */
public interface UnwrappableThrowable extends Serializable {

    /**
     * Provide a default implementation with basic output in the form of a single string.
     * The output from this method should NOT contain the stack-traces, only the top level exception listing.
     *
     * @return A string with all nested Exception reasons compressed into a single string.
     */
    default String unwrapReasons() {
        return unwrapReasons(false);
    }

    /**
     * A method to return the nested stack of exceptions, but give the option to include the stack traces to the caller.
     *
     * @param includeTrace Used to determine whether we should include the stack trace in the message or not.
     * @return A single string with all nested Exception reasons compressed into a single string,
     * which MAY have the stack trace present.
     */
    default String unwrapReasons(final boolean includeTrace) {
        return String.join("\n", unwrapReasonsIntoList(includeTrace));
    }

    /**
     * Provide a default implementation with basic output in the form of a List of strings, with an element for each
     * exception in the tree of issues.
     * The output from this method should NOT contain the stack-traces, only the top level exception listings.
     *
     * @return A collection of Strings, where each element represents one Exception in the tree of causes.
     */
    default List<String> unwrapReasonsIntoList() {
        return unwrapReasonsIntoList(false);
    }

    /**
     * The implementation of this method should provide a list, where each element in the list represents one Exception
     * in the tree of causes.
     * A caller may also use the parameter to ask for the causes to include their respective stack traces.
     * This may either be in-line to one string in the list, or it may explode them out into their own distinct entries.
     *
     * @param includeTrace Used to determine whether we should include the stack trace in the messages or not.
     * @return A collection of Strings, where each element represents one Exception in the tree of causes, which may
     * contain the stack traces that each exception originated from.
     */
    List<String> unwrapReasonsIntoList(final boolean includeTrace);
}
