package com.clumd.projects.java_common_utils.base_enhancements;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class UnwrappableRuntimeException extends RuntimeException implements UnwrappableThrowable {

    protected Collection<Object> metadata;

    public UnwrappableRuntimeException(final String reason) {
        super(reason);
    }

    public UnwrappableRuntimeException(final String reason, Throwable cause) {
        super(reason, cause);
    }

    public UnwrappableRuntimeException(final Supplier<String> reason) {
        super(reason.get());
    }

    public UnwrappableRuntimeException(final Supplier<String> reason, Throwable cause) {
        super(reason.get(), cause);
    }

    public UnwrappableRuntimeException(final String reason, Object... metadata) {
        super(reason);
        this.metadata = List.of(metadata);
    }

    public UnwrappableRuntimeException(final String reason, Throwable cause, Object... metadata) {
        super(reason, cause);
        this.metadata = List.of(metadata);
    }

    public UnwrappableRuntimeException(final Supplier<String> reason, Object... metadata) {
        super(reason.get());
        this.metadata = List.of(metadata);
    }

    public UnwrappableRuntimeException(final Supplier<String> reason, Throwable cause, Object... metadata) {
        super(reason.get(), cause);
        this.metadata = List.of(metadata);
    }

    public List<String> unwrapReasonsIntoList(boolean includeTrace) {
        return UnwrappableException.unwrapReasonsIntoList(this, includeTrace);
    }
}
