package com.clumd.projects.java_common_utils.base_enhancements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class NamedThreadFactoryTest {


    private NamedThreadFactory namedThreadFactory;
    private static final String POOL_NAME = "TESTING POOL NAME";
    private static final String PREFIX = "Custom Prefix";


    @BeforeEach
    void init() {
        namedThreadFactory = new NamedThreadFactory(POOL_NAME, PREFIX);
    }

    @Test
    void testDefaultThreadName() {
        NamedThreadFactory withoutCustomPrefix = new NamedThreadFactory(POOL_NAME);
        Thread testing = withoutCustomPrefix.newThread(() -> {
        });
        assertEquals(POOL_NAME + ":handler-1", testing.getName());
    }

    @Test
    void testDefaultThreadNameCustomPrefix() {
        Thread testing = namedThreadFactory.newThread(() -> {
        });
        assertEquals(POOL_NAME + ":" + PREFIX + "-1", testing.getName());
    }

    @Test
    void testMultipleThreadsGetIncrementingID() {
        Thread one = namedThreadFactory.newThread(() -> {
        });
        Thread two = namedThreadFactory.newThread(() -> {
        });
        Thread thr = namedThreadFactory.newThread(() -> {
        });

        assertEquals(POOL_NAME + ":" + PREFIX + "-1", one.getName());
        assertEquals(POOL_NAME + ":" + PREFIX + "-2", two.getName());
        assertEquals(POOL_NAME + ":" + PREFIX + "-3", thr.getName());
    }

    @Test
    void testSettingOneOffName() {
        Thread before = namedThreadFactory.newThread(() -> {
        });
        namedThreadFactory.overrideNextThreadName("Is now overridden");
        Thread during = namedThreadFactory.newThread(() -> {
        });
        namedThreadFactory.clearOverriddenName();
        Thread after = namedThreadFactory.newThread(() -> {
        });

        assertEquals(POOL_NAME + ":" + PREFIX + "-1", before.getName());
        assertEquals("Is now overridden", during.getName());
        assertEquals(POOL_NAME + ":" + PREFIX + "-2", after.getName());
    }

    @Test
    void testThreadPriorityAlwaysDefaults() {
        final int originalPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(10);

        Thread normalPriority = namedThreadFactory.newThread(new Thread(() -> {
        }));

        assertEquals(Thread.NORM_PRIORITY, normalPriority.getPriority(), 0);

        Thread.currentThread().setPriority(originalPriority);
    }
}
