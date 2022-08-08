package com.clumd.projects.java_common_utils.logging;

import lombok.NonNull;

import java.util.Collection;
import java.util.function.Supplier;

public interface ExtendedLogger {

    void setSelfAndChildrenToLevel(@NonNull final LogLevel selectedLevel);

    void log(@NonNull final LogLevel level, @NonNull final String message);
    void log(@NonNull final LogLevel level, @NonNull final Supplier<String> messageSupplier);

    void log(@NonNull final LogLevel level, @NonNull final String message, @NonNull final Throwable throwable);
    void log(@NonNull final LogLevel level, @NonNull final Supplier<String> messageSupplier, @NonNull final Throwable throwable);

    void log(@NonNull final LogLevel level, @NonNull final String message, @NonNull final Object param);
    void log(@NonNull final LogLevel level, @NonNull final Supplier<String> messageSupplier, @NonNull final Object param);

    void log(@NonNull final LogLevel level, @NonNull final String message, @NonNull final Collection<Object> params);
    void log(@NonNull final LogLevel level, @NonNull final Supplier<String> messageSupplier, @NonNull final Collection<Object> params);
}
