package com.clumd.projects.java_common_utils;

import lombok.NonNull;

/**
 * Inspired from: https://stackoverflow.com/questions/2596493/junit-assert-in-thread-throws-exception
 */
public class AsyncTestThread {

    private final Thread thread;
    private AssertionError caughtAssertion;
    private Exception caughtException;

    public AsyncTestThread(@NonNull final Runnable runnable) {
        thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (AssertionError e) {
                caughtAssertion = e;
            } catch (Exception e) {
                caughtException = e;
            }
        });
    }

    public void start() {
        thread.start();
    }

    public void interrupt() {
        thread.interrupt();
    }

    public void check() {
        if (caughtAssertion != null) {
            throw caughtAssertion;
        }
        if (caughtException != null) {
            throw new RuntimeException(caughtException);
        }
    }

    public void finalise() throws InterruptedException {
        thread.join();
        check();
    }
}
