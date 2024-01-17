package com.clumd.projects.java_common_utils.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PairTest {

    @Test
    void getLeft() {
        Pair<String, Integer> pair = new Pair<>("something", 1);
        assertEquals("something", pair.getLeft());
    }

    @Test
    void getRight() {
        Pair<Integer, Long> pair = new Pair<>(5, 528L);
        assertEquals(528L, pair.getRight(), 0);
    }

    @Test
    void getFirst() {
        Pair<String, Integer> pair = new Pair<>("something", 1);
        assertEquals("something", pair.getFirst());
    }

    @Test
    void getSecond() {
        Pair<Integer, Long> pair = new Pair<>(5, 528L);
        assertEquals(528L, pair.getSecond(), 0);
    }

    @Test
    void getKey() {
        Pair<String, Integer> pair = new Pair<>("something", 1);
        assertEquals("something", pair.getKey());
    }

    @Test
    void getValue() {
        Pair<Integer, Long> pair = new Pair<>(5, 528L);
        assertEquals(528L, pair.getValue(), 0);
    }

    @Test
    void setLeft() {
        Pair<String, String> left = Pair.of("not updated", "stay same");
        left.setLeft("updated");
        assertEquals("updated", left.getLeft());
        assertEquals("stay same", left.getRight());
    }

    @Test
    void setRight() {
        Pair<String, String> left = Pair.of("stay same", "not updated");
        left.setRight("updated");
        assertEquals("stay same", left.getLeft());
        assertEquals("updated", left.getRight());
    }

    @Test
    void setFirst() {
        Pair<String, String> left = Pair.of("not updated", "stay same");
        left.setFirst("updated");
        assertEquals("updated", left.getFirst());
        assertEquals("stay same", left.getSecond());
    }

    @Test
    void setSecond() {
        Pair<String, String> left = Pair.of("stay same", "not updated");
        left.setSecond("updated");
        assertEquals("stay same", left.getFirst());
        assertEquals("updated", left.getSecond());
    }

    @Test
    void setKey() {
        Pair<String, String> left = Pair.of("not updated", "stay same");
        left.setKey("updated");
        assertEquals("updated", left.getKey());
        assertEquals("stay same", left.getValue());
    }

    @Test
    void setValue() {
        Pair<String, String> left = Pair.of("stay same", "not updated");
        left.setValue("updated");
        assertEquals("stay same", left.getKey());
        assertEquals("updated", left.getValue());
    }

    @Test
    void testStaticOfCreates() {
        assertEquals(new Pair<>(5, 528L), Pair.of(5, 528L));
    }

    @Test
    void testEquals() {
        assertEquals(new Pair<>("left", "right"), new Pair<>("left", "right"));
        assertEquals(new Pair<>(false, false), new Pair<>(false, false));

        assertNotEquals(new Pair<>("left", "right"), new Pair<>("right", "left"));
        assertNotEquals(new Pair<>(false, false), new Pair<>(false, "true"));
    }

    @Test
    void testToString() {
        assertEquals("< l : r >", new Pair<>("l", "r").toString());
    }

    @Test
    void testToStringWithObjects() {
        assertEquals("< someStr : 123.456 >", new Pair<>("someStr", BigDecimal.valueOf(123.456)).toString());
    }

    @Test
    void coverage() {
        assertNotEquals(Pair.of("diff", "erent").hashCode(), Pair.of("not diff", "erent").hashCode(), 0);
        assertEquals(Pair.of("not diff", "erent").hashCode(), Pair.of("not diff", "erent").hashCode(), 0);
    }
}
