package com.clumd.projects.java_common_utils.base_enhancements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class UnwrappableThrowableTest {

    protected static class WrappedCheckedImpl extends UnwrappableException {

        public WrappedCheckedImpl(String reason) {
            super(reason);
        }

        public WrappedCheckedImpl(String reason, Throwable cause) {
            super(reason, cause);
        }

        public WrappedCheckedImpl(Supplier<String> reason) {
            super(reason);
        }

        public WrappedCheckedImpl(Supplier<String> reason, Throwable cause) {
            super(reason, cause);
        }

        public WrappedCheckedImpl(String reason, Object... metadata) {
            super(reason, metadata);
        }

        public WrappedCheckedImpl(String reason, Throwable cause, Object... metadata) {
            super(reason, cause, metadata);
        }

        public WrappedCheckedImpl(Supplier<String> reason, Object... metadata) {
            super(reason, metadata);
        }

        public WrappedCheckedImpl(Supplier<String> reason, Throwable cause, Object... metadata) {
            super(reason, cause, metadata);
        }

        public Collection<Object> getMetadata() {
            return metadata;
        }
    }

    protected static class WrappedRuntimeImpl extends UnwrappableRuntimeException {

        public WrappedRuntimeImpl(String reason) {
            super(reason);
        }

        public WrappedRuntimeImpl(String reason, Throwable cause) {
            super(reason, cause);
        }

        public WrappedRuntimeImpl(Supplier<String> reason) {
            super(reason);
        }

        public WrappedRuntimeImpl(Supplier<String> reason, Throwable cause) {
            super(reason, cause);
        }

        public WrappedRuntimeImpl(String reason, Object... metadata) {
            super(reason, metadata);
        }

        public WrappedRuntimeImpl(String reason, Throwable cause, Object... metadata) {
            super(reason, cause, metadata);
        }

        public WrappedRuntimeImpl(Supplier<String> reason, Object... metadata) {
            super(reason, metadata);
        }

        public WrappedRuntimeImpl(Supplier<String> reason, Throwable cause, Object... metadata) {
            super(reason, cause, metadata);
        }

        public Collection<Object> getMetadata() {
            return metadata;
        }
    }

    private UnwrappableException checkedExceptionMessage;
    private UnwrappableException checkedExceptionMessageAndThrowable;
    private UnwrappableException checkedDeeplyNestedException;

    private UnwrappableRuntimeException runtimeExceptionMessage;
    private UnwrappableRuntimeException runtimeExceptionMessageAndThrowable;
    private UnwrappableRuntimeException runtimeDeeplyNestedException;


    @BeforeEach
    void setUp() {
        checkedExceptionMessage = new WrappedCheckedImpl("reason");
        checkedExceptionMessageAndThrowable = new WrappedCheckedImpl("reason2", new NullPointerException("Something was null"));
        checkedDeeplyNestedException = new WrappedCheckedImpl(
                "base reason",
                new WrappedRuntimeImpl(
                        "the second reason",
                        new IllegalArgumentException(
                                "Third Reason",
                                new NullPointerException(
                                        "final Reason"
                                )
                        )
                )
        );
        runtimeExceptionMessage = new WrappedRuntimeImpl("reason");
        runtimeExceptionMessageAndThrowable = new WrappedRuntimeImpl("reason2", new NullPointerException("Something was null"));
        runtimeDeeplyNestedException = new WrappedRuntimeImpl(
                "base reason",
                new WrappedCheckedImpl(
                        "the second reason",
                        new IllegalArgumentException(
                                "Third Reason",
                                new NullPointerException(
                                        "final Reason"
                                )
                        )
                )
        );
    }

    @Test
    void simpleExceptionMessageOnlyTest_checked() {
        assertEquals("reason", checkedExceptionMessage.getMessage());
        assertNull(checkedExceptionMessage.getCause());
    }

    @Test
    void simpleExceptionErrorCodeOnlyTest_checked() {
        assertEquals("reason", checkedExceptionMessage.getMessage());
        assertNull(checkedExceptionMessage.getCause());
    }

    @Test
    void simpleExceptionTest_checked() {
        assertEquals("reason2", checkedExceptionMessageAndThrowable.getMessage());
        assertEquals("Something was null", checkedExceptionMessageAndThrowable.getCause().getMessage());
    }


    @Test
    void test_unwrapping_reasons_defaults_to_trace_false_checked() {
        assertEquals(
                """
                        Exception:  (WrappedCheckedImpl) base reason
                        Nested Exception:  (WrappedRuntimeImpl) the second reason
                        Nested Exception:  (IllegalArgumentException) Third Reason
                        Nested Exception:  (NullPointerException) final Reason"""
                , checkedDeeplyNestedException.unwrapReasons()
        );
    }

    @Test
    void test_unwrapping_reasons_with_trace_true_checked() {
        int withoutTraceLength = """
                Exception:  (WrappedCheckedImpl) base reason
                Nested Exception:  (WrappedRuntimeImpl) the second reason
                Nested Exception:  (IllegalArgumentException) Third Reason
                Nested Exception:  (NullPointerException) final Reason""".length();
        assertTrue(
                checkedDeeplyNestedException.unwrapReasons(true).length() > (withoutTraceLength * 3)
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasons(true).contains("Exception:  (WrappedCheckedImpl) base reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasons(true).contains("Nested Exception:  (WrappedRuntimeImpl) the second reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasons(true).contains("Nested Exception:  (IllegalArgumentException) Third Reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasons(true).contains("Nested Exception:  (NullPointerException) final Reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasons(true).contains("org.junit.jupiter.engine")
        );
    }

    @Test
    void test_unwrapping_reasons_into_list_defaults_to_trace_false_checked() {
        assertEquals(
                List.of("Exception:  (WrappedCheckedImpl) base reason",
                        "Nested Exception:  (WrappedRuntimeImpl) the second reason",
                        "Nested Exception:  (IllegalArgumentException) Third Reason",
                        "Nested Exception:  (NullPointerException) final Reason"
                ), checkedDeeplyNestedException.unwrapReasonsIntoList()
        );
    }

    @Test
    void test_unwrapping_reasons_into_list_with_trace_true_checked() {
        List<String> withoutTraces = List.of("Exception:  (WrappedCheckedImpl) base reason",
                "Nested Exception:  (WrappedRuntimeImpl) the second reason",
                "Nested Exception:  (IllegalArgumentException) Third Reason",
                "Nested Exception:  (NullPointerException) final Reason");
        assertTrue(
                checkedDeeplyNestedException.unwrapReasonsIntoList(true).size() > (withoutTraces.size() * 3)
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasonsIntoList(true).contains("Exception:  (WrappedCheckedImpl) base reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasonsIntoList(true).contains("Nested Exception:  (WrappedRuntimeImpl) the second reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasonsIntoList(true).contains("Nested Exception:  (IllegalArgumentException) Third Reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasonsIntoList(true).contains("Nested Exception:  (NullPointerException) final Reason")
        );
        assertTrue(
                checkedDeeplyNestedException.unwrapReasonsIntoList(true).stream().anyMatch(s -> s.contains("org.junit.jupiter.engine"))
        );
    }




    @Test
    void simpleExceptionMessageOnlyTest_runtime() {
        assertEquals("reason", runtimeExceptionMessage.getMessage());
        assertNull(runtimeExceptionMessage.getCause());
    }

    @Test
    void simpleExceptionErrorCodeOnlyTest_runtime() {
        assertEquals("reason", runtimeExceptionMessage.getMessage());
        assertNull(runtimeExceptionMessage.getCause());
    }

    @Test
    void simpleExceptionTest_runtime() {
        assertEquals("reason2", runtimeExceptionMessageAndThrowable.getMessage());
        assertEquals("Something was null", runtimeExceptionMessageAndThrowable.getCause().getMessage());
    }


    @Test
    void test_unwrapping_reasons_defaults_to_trace_false_runtime() {
        assertEquals(
                """
                        Exception:  (WrappedRuntimeImpl) base reason
                        Nested Exception:  (WrappedCheckedImpl) the second reason
                        Nested Exception:  (IllegalArgumentException) Third Reason
                        Nested Exception:  (NullPointerException) final Reason"""
                , runtimeDeeplyNestedException.unwrapReasons()
        );
    }

    @Test
    void test_unwrapping_reasons_with_trace_true_runtime() {
        int withoutTraceLength = """
                Exception:  (WrappedRuntimeImpl) base reason
                Nested Exception:  (WrappedCheckedImpl) the second reason
                Nested Exception:  (IllegalArgumentException) Third Reason
                Nested Exception:  (NullPointerException) final Reason""".length();
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasons(true).length() > (withoutTraceLength * 3)
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasons(true).contains("Exception:  (WrappedRuntimeImpl) base reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasons(true).contains("Nested Exception:  (WrappedCheckedImpl) the second reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasons(true).contains("Nested Exception:  (IllegalArgumentException) Third Reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasons(true).contains("Nested Exception:  (NullPointerException) final Reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasons(true).contains("org.junit.jupiter.engine")
        );
    }

    @Test
    void test_unwrapping_reasons_into_list_defaults_to_trace_false_runtime() {
        assertEquals(
                List.of("Exception:  (WrappedRuntimeImpl) base reason",
                        "Nested Exception:  (WrappedCheckedImpl) the second reason",
                        "Nested Exception:  (IllegalArgumentException) Third Reason",
                        "Nested Exception:  (NullPointerException) final Reason"
                ), runtimeDeeplyNestedException.unwrapReasonsIntoList()
        );
    }

    @Test
    void test_unwrapping_reasons_into_list_with_trace_true_runtime() {
        List<String> withoutTraces = List.of("Exception:  (WrappedRuntimeImpl) base reason",
                "Nested Exception:  (WrappedCheckedImpl) the second reason",
                "Nested Exception:  (IllegalArgumentException) Third Reason",
                "Nested Exception:  (NullPointerException) final Reason");
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasonsIntoList(true).size() > (withoutTraces.size() * 3)
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasonsIntoList(true).contains("Exception:  (WrappedRuntimeImpl) base reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasonsIntoList(true).contains("Nested Exception:  (WrappedCheckedImpl) the second reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasonsIntoList(true).contains("Nested Exception:  (IllegalArgumentException) Third Reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasonsIntoList(true).contains("Nested Exception:  (NullPointerException) final Reason")
        );
        assertTrue(
                runtimeDeeplyNestedException.unwrapReasonsIntoList(true).stream().anyMatch(s -> s.contains("org.junit.jupiter.engine"))
        );
    }
}
