package com.clumd.projects.java_common_utils.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ImmutablePairTest extends PairTest {

    @Test
    void testSetLeftThrowsException() {
        try {
            ImmutablePair.of("l", "r")
                    .setLeft("updated");
            fail("Previous method call should have thrown an exception.");
        } catch (UnsupportedOperationException e) {
            assertEquals("Field setting not supported on ImmutablePair.", e.getMessage());
        }
    }

    @Test
    void testSetRightThrowsException() {
        try {
            ImmutablePair.of("l", "r")
                    .setRight("updated");
            fail("Previous method call should have thrown an exception.");
        } catch (UnsupportedOperationException e) {
            assertEquals("Field setting not supported on ImmutablePair.", e.getMessage());
        }
    }

    @Test
    void testSetFirstThrowsException() {
        try {
            ImmutablePair.of("l", "r")
                    .setFirst("updated");
            fail("Previous method call should have thrown an exception.");
        } catch (UnsupportedOperationException e) {
            assertEquals("Field setting not supported on ImmutablePair.", e.getMessage());
        }
    }

    @Test
    void testSetSecondThrowsException() {
        try {
            ImmutablePair.of("l", "r")
                    .setSecond("updated");
            fail("Previous method call should have thrown an exception.");
        } catch (UnsupportedOperationException e) {
            assertEquals("Field setting not supported on ImmutablePair.", e.getMessage());
        }
    }

    @Test
    void testSetKeyThrowsException() {
        try {
            ImmutablePair.of("l", "r")
                    .setKey("updated");
            fail("Previous method call should have thrown an exception.");
        } catch (UnsupportedOperationException e) {
            assertEquals("Field setting not supported on ImmutablePair.", e.getMessage());
        }
    }

    @Test
    void testSetValueThrowsException() {
        try {
            ImmutablePair.of("l", "r")
                    .setValue("updated");
            fail("Previous method call should have thrown an exception.");
        } catch (UnsupportedOperationException e) {
            assertEquals("Field setting not supported on ImmutablePair.", e.getMessage());
        }
    }
}
