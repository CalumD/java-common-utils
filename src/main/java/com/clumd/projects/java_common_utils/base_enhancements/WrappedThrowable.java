package com.clumd.projects.java_common_utils.base_enhancements;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class WrappedThrowable extends Throwable {

    protected Collection<Object> metadata;

    public WrappedThrowable(final String reason) {
        super(reason);
    }

    public WrappedThrowable(final String reason, Throwable cause) {
        super(reason, cause);
    }

    public WrappedThrowable(final Supplier<String> reason) {
        super(reason.get());
    }

    public WrappedThrowable(final Supplier<String> reason, Throwable cause) {
        super(reason.get(), cause);
    }

    public WrappedThrowable(final String reason, Object... metadata) {
        super(reason);
        this.metadata = List.of(metadata);
    }

    public WrappedThrowable(final String reason, Throwable cause, Object... metadata) {
        super(reason, cause);
        this.metadata = List.of(metadata);
    }

    public WrappedThrowable(final Supplier<String> reason, Object... metadata) {
        super(reason.get());
        this.metadata = List.of(metadata);
    }

    public WrappedThrowable(final Supplier<String> reason, Throwable cause, Object... metadata) {
        super(reason.get(), cause);
        this.metadata = List.of(metadata);
    }

    public String unrwapIntoString() {
        return unwrapIntoString(false);
    }

    public String unwrapIntoString(boolean withStackTrace) {
        return String.join("\n", unwrapIntoStrings(withStackTrace));
    }

    public List<String> unwrapIntoStrings() {
        return unwrapIntoStrings(false);
    }

    public abstract List<String> unwrapIntoStrings(boolean withStackTrace);
}
