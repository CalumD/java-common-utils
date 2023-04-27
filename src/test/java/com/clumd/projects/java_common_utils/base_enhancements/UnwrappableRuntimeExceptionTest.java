package com.clumd.projects.java_common_utils.base_enhancements;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnwrappableRuntimeExceptionTest extends UnwrappableThrowableTest {

    @Test
    void test_onlyReason() {
        WrappedRuntimeImpl exception = new WrappedRuntimeImpl("some reason");
        assertEquals("some reason", exception.getMessage());
        assertNull(exception.getCause());
        assertNull(exception.getMetadata());
    }

    @Test
    void test_reasonAndThrowable() {
        WrappedRuntimeImpl exception = new WrappedRuntimeImpl("some reason", new WrappedRuntimeImpl("nested reason to check"));
        assertEquals("some reason", exception.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), exception.getCause().getMessage());
        assertNull(exception.getMetadata());
    }

    @Test
    void test_reasonSupplier() {
        WrappedRuntimeImpl exception = new WrappedRuntimeImpl(() -> "some supplied reason");
        assertEquals("some supplied reason", exception.getMessage());
        assertNull(exception.getCause());
        assertNull(exception.getMetadata());
    }

    @Test
    void test_reasonSupplierAndThrowable() {
        WrappedRuntimeImpl exception = new WrappedRuntimeImpl(() -> "some supplied reason", new WrappedRuntimeImpl("nested reason to check"));
        assertEquals("some supplied reason", exception.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), exception.getCause().getMessage());
        assertNull(exception.getMetadata());
    }

    @Test
    void test_reasonAndMetadata() {
        WrappedRuntimeImpl v1 = new WrappedRuntimeImpl("some reason 1", 123L);
        WrappedRuntimeImpl v2 = new WrappedRuntimeImpl("some reason 2", new Object[] {1, 2, 3});
        WrappedRuntimeImpl v3 = new WrappedRuntimeImpl("some reason 3", List.of("1", "2", "3"));
        WrappedRuntimeImpl v4 = new WrappedRuntimeImpl("some reason 4", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals("some reason 1", v1.getMessage());
        assertNull(v1.getCause());
        assertEquals(1, v1.getMetadata().size(), 0);
        assertTrue(v1.getMetadata().stream().findFirst().isPresent());
        assertEquals(123L, (long)v1.getMetadata().stream().findFirst().get(), 0);

        assertEquals("some reason 2", v2.getMessage());
        assertNull(v2.getCause());
        assertEquals(3, v2.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(3));

        assertEquals("some reason 3", v3.getMessage());
        assertNull(v3.getCause());
        assertEquals(1, v3.getMetadata().size(), 0);
        assertTrue(v3.getMetadata().stream().findFirst().isPresent());
        assertEquals(List.of(List.of("1", "2", "3")), v3.getMetadata());

        assertEquals("some reason 4", v4.getMessage());
        assertNull(v4.getCause());
        assertEquals(10, v4.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(3));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(4));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(5));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(6));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(7));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(8));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(9));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(10));
    }

    @Test
    void test_reasonThrowableAndMetadata() {
        WrappedRuntimeImpl v1 = new WrappedRuntimeImpl("some reason 1", new WrappedRuntimeImpl("nested reason to check"), 123L);
        WrappedRuntimeImpl v2 = new WrappedRuntimeImpl("some reason 2", new WrappedRuntimeImpl("nested reason to check"), new Object[] {1, 2, 3});
        WrappedRuntimeImpl v3 = new WrappedRuntimeImpl("some reason 3", new WrappedRuntimeImpl("nested reason to check"), List.of("1", "2", "3"));
        WrappedRuntimeImpl v4 = new WrappedRuntimeImpl("some reason 4", new WrappedRuntimeImpl("nested reason to check"), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals("some reason 1", v1.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v1.getCause().getMessage());
        assertEquals(1, v1.getMetadata().size(), 0);
        assertTrue(v1.getMetadata().stream().findFirst().isPresent());
        assertEquals(123L, (long)v1.getMetadata().stream().findFirst().get(), 0);

        assertEquals("some reason 2", v2.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v2.getCause().getMessage());
        assertEquals(3, v2.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(3));

        assertEquals("some reason 3", v3.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v3.getCause().getMessage());
        assertEquals(1, v3.getMetadata().size(), 0);
        assertTrue(v3.getMetadata().stream().findFirst().isPresent());
        assertEquals(List.of(List.of("1", "2", "3")), v3.getMetadata());

        assertEquals("some reason 4", v4.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v4.getCause().getMessage());
        assertEquals(10, v4.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(3));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(4));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(5));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(6));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(7));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(8));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(9));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(10));
    }

    @Test
    void test_reasonSupplierAndMetadata() {
        WrappedRuntimeImpl v1 = new WrappedRuntimeImpl(() -> "some supplied reason 1", 123L);
        WrappedRuntimeImpl v2 = new WrappedRuntimeImpl(() -> "some supplied reason 2", new Object[] {1, 2, 3});
        WrappedRuntimeImpl v3 = new WrappedRuntimeImpl(() -> "some supplied reason 3", List.of("1", "2", "3"));
        WrappedRuntimeImpl v4 = new WrappedRuntimeImpl(() -> "some supplied reason 4", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals("some supplied reason 1", v1.getMessage());
        assertNull(v1.getCause());
        assertEquals(1, v1.getMetadata().size(), 0);
        assertTrue(v1.getMetadata().stream().findFirst().isPresent());
        assertEquals(123L, (long)v1.getMetadata().stream().findFirst().get(), 0);

        assertEquals("some supplied reason 2", v2.getMessage());
        assertNull(v2.getCause());
        assertEquals(3, v2.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(3));

        assertEquals("some supplied reason 3", v3.getMessage());
        assertNull(v3.getCause());
        assertEquals(1, v3.getMetadata().size(), 0);
        assertTrue(v3.getMetadata().stream().findFirst().isPresent());
        assertEquals(List.of(List.of("1", "2", "3")), v3.getMetadata());

        assertEquals("some supplied reason 4", v4.getMessage());
        assertNull(v4.getCause());
        assertEquals(10, v4.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(3));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(4));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(5));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(6));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(7));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(8));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(9));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(10));
    }

    @Test
    void test_reasonSupplierThrowableAndMetadata() {
        WrappedRuntimeImpl v1 = new WrappedRuntimeImpl(() -> "some supplied reason 1", new WrappedRuntimeImpl("nested reason to check"), 123L);
        WrappedRuntimeImpl v2 = new WrappedRuntimeImpl(() -> "some supplied reason 2", new WrappedRuntimeImpl("nested reason to check"), new Object[] {1, 2, 3});
        WrappedRuntimeImpl v3 = new WrappedRuntimeImpl(() -> "some supplied reason 3", new WrappedRuntimeImpl("nested reason to check"), List.of("1", "2", "3"));
        WrappedRuntimeImpl v4 = new WrappedRuntimeImpl(() -> "some supplied reason 4", new WrappedRuntimeImpl("nested reason to check"), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals("some supplied reason 1", v1.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v1.getCause().getMessage());
        assertEquals(1, v1.getMetadata().size(), 0);
        assertTrue(v1.getMetadata().stream().findFirst().isPresent());
        assertEquals(123L, (long)v1.getMetadata().stream().findFirst().get(), 0);

        assertEquals("some supplied reason 2", v2.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v2.getCause().getMessage());
        assertEquals(3, v2.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v2.getMetadata()).contains(3));

        assertEquals("some supplied reason 3", v3.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v3.getCause().getMessage());
        assertEquals(1, v3.getMetadata().size(), 0);
        assertTrue(v3.getMetadata().stream().findFirst().isPresent());
        assertEquals(List.of(List.of("1", "2", "3")), v3.getMetadata());

        assertEquals("some supplied reason 4", v4.getMessage());
        assertEquals(new WrappedRuntimeImpl("nested reason to check").getMessage(), v4.getCause().getMessage());
        assertEquals(10, v4.getMetadata().size(), 0);
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(1));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(2));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(3));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(4));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(5));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(6));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(7));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(8));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(9));
        assertTrue(new ArrayList<>(v4.getMetadata()).contains(10));
    }
}
