package com.clumd.projects.java_common_utils.base_enhancements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class UnwrappableException extends Exception implements UnwrappableThrowable {

    protected Collection<Object> metadata;

    protected UnwrappableException(final String reason) {
        super(reason);
    }

    protected UnwrappableException(final Iterable<String> reasons) {
        this(String.join(",\n", reasons));
    }

    protected UnwrappableException(final String reason, final Throwable cause) {
        super(reason, cause);
    }

    protected UnwrappableException(final Iterable<String> reasons, final Throwable cause) {
        this(String.join(",\n", reasons), cause);
    }

    protected UnwrappableException(final Supplier<String> reason) {
        super(reason.get());
    }

    protected UnwrappableException(final Supplier<String> reason, final Throwable cause) {
        super(reason.get(), cause);
    }

    protected UnwrappableException(final String reason, final Object... metadata) {
        super(reason);
        this.metadata = List.of(metadata);
    }

    protected UnwrappableException(final Iterable<String> reasons, final Object... metadata) {
        this(String.join(",\n", reasons));
        this.metadata = List.of(metadata);
    }

    protected UnwrappableException(final String reason, final Throwable cause, final Object... metadata) {
        super(reason, cause);
        this.metadata = List.of(metadata);
    }

    protected UnwrappableException(final Iterable<String> reasons, final Throwable cause, final Object... metadata) {
        this(String.join(",\n", reasons), cause);
        this.metadata = List.of(metadata);
    }

    protected UnwrappableException(final Supplier<String> reason, final Object... metadata) {
        super(reason.get());
        this.metadata = List.of(metadata);
    }

    protected UnwrappableException(final Supplier<String> reason, final Throwable cause, final Object... metadata) {
        super(reason.get(), cause);
        this.metadata = List.of(metadata);
    }

    public static List<String> unwrapReasonsIntoList(final Throwable rootCause, final boolean includeTrace) {
        List<String> reasons = new ArrayList<>();

        Throwable throwable = rootCause;
        boolean isTopReason = true;
        StringBuilder lineOfReasoning = new StringBuilder();
        do {
            if (isTopReason) {
                lineOfReasoning.append("Exception:  ");
                isTopReason = false;
            } else {
                lineOfReasoning.append("Nested Exception:  ");
            }
            lineOfReasoning.append('(').append(throwable.getClass().getSimpleName()).append(") ");
            lineOfReasoning.append(throwable.getMessage());
            reasons.add(lineOfReasoning.toString());

            if (includeTrace) {
                for (Object stackTraceLine : throwable.getStackTrace()) {
                    reasons.add("  " + stackTraceLine.toString());
                }
            }

            lineOfReasoning = new StringBuilder();
            throwable = throwable.getCause();
        } while (throwable != null && throwable != throwable.getCause());

        return reasons;
    }

    @Override
    public List<String> unwrapReasonsIntoList(final boolean includeTrace) {
        return unwrapReasonsIntoList(this, includeTrace);
    }
}
