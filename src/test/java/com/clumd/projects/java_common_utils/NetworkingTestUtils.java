package com.clumd.projects.java_common_utils;

import lombok.NoArgsConstructor;

import java.net.BindException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@NoArgsConstructor
public class NetworkingTestUtils {

    public static final int FIRST_SERVER_PORT;
    public static final int SECOND_SERVER_PORT;
    public static final List<Integer> CLIENT_PORTS;

    private static final int GLOBAL_SLEEP_TIMER;

    static {
        // Determine how long we should sleep for on the current system to ensure enough time for reliable test execution when dealing with threads.
        if ("true".equals(System.getenv("CI"))) {
            switch (System.getenv("RUNNER_OS")) {
                case "Linux" -> GLOBAL_SLEEP_TIMER = 750;
                case "macOS" -> GLOBAL_SLEEP_TIMER = 1000;
                default -> GLOBAL_SLEEP_TIMER = 1750;
            }
        } else {
            GLOBAL_SLEEP_TIMER = 200;
        }

        // Determine a collection of sequentially available ports to use when testing network sockets.
        int startingPort = 2000, requiredPorts = 10;
        int consecutiveAvailablePortCount = 0;

        while (consecutiveAvailablePortCount < requiredPorts) {
            try (
                    ServerSocket s1 = new ServerSocket(startingPort + consecutiveAvailablePortCount);
                    ServerSocket s2 = new ServerSocket(startingPort + consecutiveAvailablePortCount + 1)
            ) {
                consecutiveAvailablePortCount += 2;
            } catch (BindException e) {
                if (e.getMessage().contains("Address already in use")) {
                    startingPort += consecutiveAvailablePortCount + 1;
                    consecutiveAvailablePortCount = 0;
                } else {
                    fail(e);
                }
            } catch (Exception e) {
                fail(e);
            }
        }

        FIRST_SERVER_PORT = startingPort;
        SECOND_SERVER_PORT = startingPort + 1;
        List<Integer> clientPorts = new ArrayList<>(requiredPorts);
        for (startingPort = startingPort + 2; startingPort < FIRST_SERVER_PORT + requiredPorts; startingPort++) {
            clientPorts.add(startingPort);
        }
        CLIENT_PORTS = Collections.unmodifiableList(clientPorts);
    }

    @SuppressWarnings("squid:S2925")
    public static void sleep() {
        try {
            Thread.sleep(GLOBAL_SLEEP_TIMER);
        } catch (InterruptedException e) {
            fail("Test thread was interrupted before it could complete.");
        }
    }

    @SuppressWarnings("squid:S2925")
    public static void sleep(final double timeInSeconds) {
        try {
            Thread.sleep((long) (timeInSeconds * 1000));
        } catch (InterruptedException e) {
            fail("Test thread was interrupted before it could complete.");
        }
    }
}
