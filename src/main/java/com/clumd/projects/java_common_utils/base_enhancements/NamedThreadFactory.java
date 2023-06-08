package com.clumd.projects.java_common_utils.base_enhancements;

import com.clumd.projects.java_common_utils.logging.LogRoot;
import lombok.NonNull;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is used in order to re-name the pool and workers who are spawned into it.
 */
public class NamedThreadFactory implements ThreadFactory {

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

        // Alert the LogRoot of this thread, and it's name.
        LogRoot.updateThreadIdName(thread.threadId(), thread.getName());

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
     * Used in the event that you want to cancel a thread name overriding.
     */
    public void clearOverriddenName() {
        this.nextThreadOverriddenName = null;
    }
}
