package com.clumd.projects.java_common_utils.base_enhancements;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class UnwrappableRuntimeException extends RuntimeException implements UnwrappableThrowable {

    protected Collection<Object> metadata;

    protected UnwrappableRuntimeException(final String reason) {
        super(reason);
    }

    protected UnwrappableRuntimeException(final Iterable<String> reasons) {
        this(String.join(",\n", reasons));
    }

    protected UnwrappableRuntimeException(final String reason, final Throwable cause) {
        super(reason, cause);
    }

    protected UnwrappableRuntimeException(final Iterable<String> reasons, final Throwable cause) {
        this(String.join(",\n", reasons), cause);
    }

    protected UnwrappableRuntimeException(final Supplier<String> reason) {
        super(reason.get());
    }

    protected UnwrappableRuntimeException(final Supplier<String> reason, final Throwable cause) {
        super(reason.get(), cause);
    }

    protected UnwrappableRuntimeException(final String reason, final Object... metadata) {
        super(reason);
        this.metadata = List.of(metadata);
    }

    protected UnwrappableRuntimeException(final Iterable<String> reasons, final Object... metadata) {
        this(String.join(",\n", reasons));
        this.metadata = List.of(metadata);
    }

    protected UnwrappableRuntimeException(final String reason, final Throwable cause, final Object... metadata) {
        super(reason, cause);
        this.metadata = List.of(metadata);
    }

    protected UnwrappableRuntimeException(final Iterable<String> reasons, final Throwable cause, final Object... metadata) {
        this(String.join(",\n", reasons), cause);
        this.metadata = List.of(metadata);
    }

    protected UnwrappableRuntimeException(final Supplier<String> reason, final Object... metadata) {
        super(reason.get());
        this.metadata = List.of(metadata);
    }

    protected UnwrappableRuntimeException(final Supplier<String> reason, final Throwable cause, final Object... metadata) {
        super(reason.get(), cause);
        this.metadata = List.of(metadata);
    }

    @Override
    public List<String> unwrapReasonsIntoList(boolean includeTrace) {
        return UnwrappableException.unwrapReasonsIntoList(this, includeTrace);
    }
}
