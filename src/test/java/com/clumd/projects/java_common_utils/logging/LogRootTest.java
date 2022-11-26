package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.files.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogRootTest {

    private static final String LOGGING_TEST_PATH = "src/test/resources/logging/logRoot";
    private static final String DISCARDABLE_PACKAGE = "com.clumd.projects.java_common_utils.logging.";

    @BeforeEach
    void setup() throws IOException {
        FileUtils.deleteDirectoryIfExists(LOGGING_TEST_PATH);
        LogRoot.init(
                        DISCARDABLE_PACKAGE,
                        "L_T_R")
                .withHandlers(List.of(
                        LogRoot.basicFileHandler(LOGGING_TEST_PATH),
                        LogRoot.basicConsoleHandler(true)
                ));
    }

    @AfterEach
    void tearDown() {
        LogManager.getLogManager().reset();
    }

    @Test
    void checkNewLoggersArePrefixedWithLTR() {
        assertEquals("L_T_R.some.class.name", LogRoot.createLogger("some.class.name").getName());
    }

    @Test
    void checkLoggersRemoveComDotClumd() {
        assertEquals("L_T_R.myFirstName", LogRoot.createLogger("com.clumd.projects.java_common_utils.logging.myFirstName").getName());
    }

    @Test
    void checkLoggerCustomPrefixName() {
        assertEquals("L_T_R.customPrefix:actual.logger.identifier",
                LogRoot.createLogger("customPrefix", "actual.logger.identifier").getName());
    }

}
