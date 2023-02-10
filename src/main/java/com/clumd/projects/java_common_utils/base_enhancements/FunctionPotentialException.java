package com.clumd.projects.java_common_utils.base_enhancements;

/**
 * An extension to {@link java.util.function.Function} which allows the apply method to throw checked exceptions if
 * desired.
 *
 * @param <T> The type of the input parameter to the function
 * @param <R> The return type of the function
 * @param <E> The typing of a potential checked exception
 */
@FunctionalInterface
public interface FunctionPotentialException<T, R, E extends Exception> {
    R apply(T t) throws E;
}
