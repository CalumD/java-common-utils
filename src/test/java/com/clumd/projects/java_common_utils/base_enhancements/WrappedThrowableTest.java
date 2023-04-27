package com.clumd.projects.java_common_utils.base_enhancements;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

class WrappedThrowableTest {

    @Test
    void test_constructors_with_variadic_objects() {
        UnwrappableThrowable v1 = new WrappedThrowableImpl("", 123);
        UnwrappableThrowable v2 = new WrappedThrowableImpl("", new Object[] {1, 2, 3});
        UnwrappableThrowable v3 = new WrappedThrowableImpl("", List.of("1", "2", "3"));
        UnwrappableThrowable v4 = new WrappedThrowableImpl("", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println("");
    }

    private static class WrappedThrowableImpl extends UnwrappableException {

        public WrappedThrowableImpl(String reason) {
            super(reason);
        }

        public WrappedThrowableImpl(String reason, Throwable cause) {
            super(reason, cause);
        }

        public WrappedThrowableImpl(Supplier<String> reason) {
            super(reason);
        }

        public WrappedThrowableImpl(Supplier<String> reason, Throwable cause) {
            super(reason, cause);
        }

        public WrappedThrowableImpl(String reason, Object... metadata) {
            super(reason, metadata);
        }

        public WrappedThrowableImpl(String reason, Throwable cause, Object... metadata) {
            super(reason, cause, metadata);
        }

        public WrappedThrowableImpl(Supplier<String> reason, Object... metadata) {
            super(reason, metadata);
        }

        public WrappedThrowableImpl(Supplier<String> reason, Throwable cause, Object... metadata) {
            super(reason, cause, metadata);
        }
    }
}
