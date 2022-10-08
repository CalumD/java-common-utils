package com.clumd.projects.java_common_utils.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.LogRecord;

class ConsoleControllerTest {

    private ConsoleController controller;
    private UUID runID;
    private String systemId;
    private Map<Long, String> overriddenThreadNames;

    @BeforeEach
    void setup() {
        controller = new ConsoleController();
        runID = UUID.randomUUID();
        systemId = "system id";
        overriddenThreadNames = new HashMap<>();
        controller.acceptLogRootRefs(runID, systemId, overriddenThreadNames);
    }

    @Test
    void test_base_format() {
        System.out.println(
                controller
                        .getFormatter()
                        .format(new LogRecord(CustomLevel.FAILURE, "blah"))
        );
    }
}
