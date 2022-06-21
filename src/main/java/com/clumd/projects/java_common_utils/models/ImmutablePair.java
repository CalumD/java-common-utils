package com.clumd.projects.java_common_utils.models;

import java.io.Serializable;

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
}
