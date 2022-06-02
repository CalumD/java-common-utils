package com.clumd.projects.java_common_utils.arg_parser;

import lombok.NonNull;

import java.text.ParseException;
import java.util.Collection;

/**
 * This represents the implementation of a class capable of parsing {@link Argument}s from the command line.
 */
public interface CLIArgParser {

    /**
     * Used to execute the parsing process of Command line arguments into validated program input.
     *
     * @param possibleArguments The collection of all possible Command Line Arguments this process MAY receive.
     * @param args              The CLI Args provided to the JVM for this process
     * @return The collection of Arguments which IS present on the CLI, along with parsed values to the options, if
     * relevant and present.
     * @throws ParseException Thrown if any of the {@link Argument}s failed to parse.
     */
    Collection<Argument<?>> parse(
            @NonNull final Collection<Argument<?>> possibleArguments,
            @NonNull final String[] args
    ) throws ParseException;

    /**
     * Used to set all the Strings which should be used if printing help text.
     *
     * @param name        The Name of the running application.
     * @param usageSyntax The acceptable syntax which should be used to invoke an instance of this program.
     * @param synopsis    A short description to explain what the running application does.
     * @param author      Who wrote the primary running application.
     * @param bugs        Where anyone using your software should report their bugs to.
     */
    void setBoilerplate(
            @NonNull final String name,
            @NonNull final String usageSyntax,
            @NonNull final String synopsis,
            @NonNull final String author,
            @NonNull final String bugs
    );
}
