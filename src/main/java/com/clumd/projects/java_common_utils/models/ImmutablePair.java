package com.clumd.projects.java_common_utils.models;

import java.io.Serializable;

/**
 * An instance of {@link Pair}, but where the values are immutable, and references cannot be changed once set. Be aware,
 * that values within these object references CAN still be updated or changed.
 *
 * @param <L> The First object within the Pair.
 * @param <R> The Second object within the Pair.
 */
public class ImmutablePair<L extends Serializable, R extends Serializable> extends Pair<L, R> implements Serializable {

    private static final String SETTER_EXCEPTION_MESSAGE = "Field setting not supported on ImmutablePair.";

    public ImmutablePair(final L left, final R right) {
        super(left, right);
    }

    public static <L extends Serializable, R extends Serializable> Pair<L, R> of(final L left, final R right) {
        return new ImmutablePair<>(left, right);
    }

    @Override
    public void setLeft(final L left) {
        throw new UnsupportedOperationException(SETTER_EXCEPTION_MESSAGE);
    }

    @Override
    public void setRight(final R right) {
        throw new UnsupportedOperationException(SETTER_EXCEPTION_MESSAGE);
    }

    @Override
    public void setFirst(final L updatedFirst) {
        throw new UnsupportedOperationException(SETTER_EXCEPTION_MESSAGE);
    }

    @Override
    public void setSecond(final R updatedSecond) {
        throw new UnsupportedOperationException(SETTER_EXCEPTION_MESSAGE);
    }

    @Override
    public void setKey(final L updatedKey) {
        throw new UnsupportedOperationException(SETTER_EXCEPTION_MESSAGE);
    }

    @Override
    public void setValue(final R updatedValue) {
        throw new UnsupportedOperationException(SETTER_EXCEPTION_MESSAGE);
    }
}
