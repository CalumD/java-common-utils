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
     * Used to give the pool a name so that log messages make more sense.
     *
     * @param poolName What to name the pool.
     */
    public NamedThreadFactory(String poolName) {
        this(poolName, "handler");
    }

    /**
     * Used to give the pool a name so that log messages make more sense - using a prefix for additional context.
     *
     * @param poolName What to name the pool.
     * @param prefix   An additional prefix onto the thread to be used for further context clarification.
     */
    public NamedThreadFactory(String poolName, String prefix) {
        this.namePrefix = poolName + ":" + prefix + "-";
    }

    public void overrideNextThreadName(@NonNull final String withName) {
        nextThreadOverriddenName = withName;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        //give our own pattern for the name - but otherwise behave exactly like the super()
        Thread thread = new Thread(
                runnable,
                Objects.requireNonNullElseGet(
                        nextThreadOverriddenName,
                        () -> this.namePrefix + this.threadNumber.getAndIncrement()
                )
        );
        nextThreadOverriddenName = null;

        LogRoot.updateThreadIdName(thread.threadId(), thread.getName());
        thread.setDaemon(Thread.currentThread().isDaemon());
        thread.setPriority(Thread.currentThread().getPriority());

        return thread;
    }

    public void clearOverriddenName() {
        this.nextThreadOverriddenName = null;
    }
}
