package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.files.FileUtils;
import com.clumd.projects.java_common_utils.logging.api.CustomLogHandler;
import com.clumd.projects.java_common_utils.logging.controllers.ConsoleController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import static com.clumd.projects.java_common_utils.logging.LogRootTest.LOGGING_TEST_PATH;
import static com.clumd.projects.java_common_utils.logging.common.CustomLevel.CRITICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogRootWithoutInitTest {

    @Captor
    private ArgumentCaptor<LogRecord> logCaptor;
    @Mock
    private LogRootWithoutInitTest.ExtendedConsoleController mockController;
    private CustomLogHandler customConsoleHandler;

    @BeforeEach
    void setup() throws IOException {
        lenient().doNothing().when(mockController).publish(logCaptor.capture());
        FileUtils.deleteDirectoryIfExists(LOGGING_TEST_PATH);
        customConsoleHandler = LogRoot.basicConsoleHandler(true);
    }

    @Test
    void test_set_log_root_without_explicitly_calling_init_with_values() throws NoSuchFieldException, IllegalAccessException {
        ExtendedLogger el = LogRoot.createLogger(LogRootWithoutInitTest.class);

        // Test the defaults got set since we haven't yet called init
        assertTrue(
                ("LogRoot.com.clumd.projects.java_common_utils.logging.LogRootWithoutInitTest".equals(el.getName()))
                        ||
                        ("L_T_R.LogRootWithoutInitTest".equals(el.getName()))
        );

        Class<LogRoot> types = LogRoot.class;
        java.lang.reflect.Field field = types.getDeclaredField("discardablePackageId");
        field.setAccessible(true);
        // Test the defaults got set since we haven't yet called init
        assertTrue(
                ("jdk.internal.".equals(field.get(types)))
                        ||
                        ("com.clumd.projects.java_common_utils.logging.".equals(field.get(types)))
        );

        // Now test the actual log message has still worked
        LogRoot.init("jdk.internal.", "TESTING")
                .withHandlers(List.of(mockController, customConsoleHandler));
        when(mockController.getFormatter()).thenReturn(((StreamHandler) customConsoleHandler).getFormatter());

        el = LogRoot.createLogger(LogRootWithoutInitTest.class);
        el.log(CRITICAL, "some log message");

        List<LogRecord> capturedLogs = logCaptor.getAllValues();
        assertEquals(1, capturedLogs.size(), 0);

        assertTrue(mockController
                .getFormatter()
                .format(capturedLogs.get(0))
                .contains("TESTING.com.clumd.projects.java_common_utils.logging.LogRootWithoutInitTest    (1):Anon/Unknown Thread   ")
        );
    }

    private static class ExtendedConsoleController extends ConsoleController {
        public ExtendedConsoleController(boolean useSpacerLines) {
            super(useSpacerLines);
        }
    }
}
