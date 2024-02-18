package com.clumd.projects.java_common_utils.base_enhancements;

import lombok.NonNull;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is used in order to re-name the pool and workers who are spawned into it.
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final NamedThreadFactory instanceForFactory = new NamedThreadFactory("pool");
    private final String namePrefix;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private String nextThreadOverriddenName = null;

    /**
     * Used to give the pool a name so that log messages can be more contextual.
     * Though the Pool name is customisable, thread names in this pool will be called "handler"s.
     *
     * @param poolName What to name the pool.
     */
    public NamedThreadFactory(String poolName) {
        this(poolName, "handler");
    }

    /**
     * Used to give the pool a name so that log messages make more sense - using a prefix for further context.
     *
     * @param poolName What to name the pool.
     * @param prefix   An additional prefix onto the thread to be used for further context clarification.
     */
    public NamedThreadFactory(String poolName, String prefix) {
        this.namePrefix = poolName + ":" + prefix + "-";
    }

    /**
     * A static version of {@link NamedThreadFactory#newThread(String, Runnable)}, to save creating a new instance if you only want one custom
     * named thread.
     *
     * @param newThreadName The name the associated thread should be given.
     * @param runnable      The actual work to carry out under the given name.
     * @return A new named thread ready to be executed.
     */
    public static Thread create(@NonNull final String newThreadName, @NonNull final Runnable runnable) {
        return instanceForFactory.newThread(newThreadName, runnable);
    }

    /**
     * Used to tell this factory that the next thread should ignore the standard naming convention and just call it
     * whatever this method is passed.
     * This should be a single-shot method, which will revert back to the naming pattern after the next thread has been
     * created with this as its name.
     *
     * @param withName The name the next thread should use.
     */
    public void overrideNextThreadName(@NonNull final String withName) {
        nextThreadOverriddenName = withName;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        // Give our own pattern for the name - but otherwise behave exactly like the super()
        Thread thread = new Thread(
                runnable,
                Objects.requireNonNullElseGet(
                        nextThreadOverriddenName,
                        () -> this.namePrefix + this.threadNumber.getAndIncrement()
                )
        );
        nextThreadOverriddenName = null;

        // replication of super's logic.
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }

        return thread;
    }

    /**
     * This is a convenience method to create a new thread from a lambda, with a given name, using a single method - rather than having to
     * explicitly call the override and clear next thread name methods.
     *
     * @param newThreadName The name the associated thread should be given.
     * @param runnable      The actual work to carry out under the given name.
     * @return A new named thread ready to be executed.
     */
    public Thread newThread(@NonNull final String newThreadName, @NonNull final Runnable runnable) {
        overrideNextThreadName(newThreadName);
        try {
            return newThread(runnable);
        } finally {
            clearOverriddenName();
        }
    }

    /**
     * Used in the event that you want to cancel a thread name overriding.
     */
    public void clearOverriddenName() {
        this.nextThreadOverriddenName = null;
    }
}
