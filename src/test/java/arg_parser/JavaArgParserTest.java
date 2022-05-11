package arg_parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaArgParserTest {

    private CLIArgParser cliArgParser;

    @BeforeEach
    void setup() {
        cliArgParser = new JavaArgParser();
    }

    @Test
    void test_empty_cli_returns_no_args() throws ParseException {
        assertTrue(cliArgParser.parse(
                        List.of(
                                Argument.builder().build()
                        ), new String[]{""}
                ).isEmpty()
        );
    }
}
