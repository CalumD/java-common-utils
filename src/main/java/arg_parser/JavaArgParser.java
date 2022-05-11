package arg_parser;

import lombok.NonNull;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JavaArgParser implements CLIArgParser {

    private static final String INDENT = "    ";
    private static final String NEWLINE = "\n";
    private static final String SPACE_LINE = NEWLINE + NEWLINE;

    @Override
    public Collection<Argument<?>> parse(
            @NonNull Collection<Argument<?>> possibleArguments,
            @NonNull String[] args
    ) throws ParseException {
        // We use a map here as the collection type, to ensure that if there are duplicate options provided on the CLI,
        // that only one (the closest to the end) will become impactful.
        Map<Integer, Argument<?>> returnArgumentMap = new HashMap<>();




        return returnArgumentMap.values();
    }

    @Override
    public void setBoilerplate(
            @NonNull String name,
            @NonNull String usageSyntax,
            @NonNull String synopsis,
            @NonNull String author,
            @NonNull String bugs
    ) {

    }
}
