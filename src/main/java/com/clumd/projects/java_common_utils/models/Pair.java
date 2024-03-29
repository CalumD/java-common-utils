package com.clumd.projects.java_common_utils.models;

import lombok.Data;

import java.io.Serializable;

/**
 * A simple 'Pair' concept, which groups together two objects, via whatever logical grouping the user believes.
 *
 * @param <L> The type of the first Pair member
 * @param <R> The type of the second Pair member
 */
@Data
public class Pair<L extends Serializable, R extends Serializable> implements Serializable {

    private L left;
    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L extends Serializable, R extends Serializable> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }

    public L getFirst() {
        return left;
    }

    public void setFirst(final L updatedFirst) {
        left = updatedFirst;
    }

    public R getSecond() {
        return right;
    }

    public void setSecond(final R updatedSecond) {
        right = updatedSecond;
    }

    public L getKey() {
        return left;
    }

    public void setKey(final L updatedKey) {
        left = updatedKey;
    }

    public R getValue() {
        return right;
    }

    public void setValue(final R updatedValue) {
        right = updatedValue;
    }

    @Override
    public String toString() {
        return "< " + left.toString() + " : " + right.toString() + " >";
    }
}
