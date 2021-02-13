package programmerstasken.qualityunit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;

public class Processor {

    private final List<WaitingTimeLine> waitingTimeLines = new ArrayList<>();
    private final List<String> output = new ArrayList<>();
    private final int maxLinesPerFile = 100000;
    
    void clear() {
        waitingTimeLines.clear();
        output.clear();
    }

    /**
     * Note: The first line of the file contains the total number of lines.
     */
    void process(Path path) throws IOException {
        clear();

        try ( Stream<String> lines = Files.lines(path)) {
            lines.skip(1).limit(maxLinesPerFile).forEach(this::process);
        }
    }

    void process(String line) {
        if (line.isEmpty()) {
            return;
        }

        switch (line.charAt(0)) {
            case 'C':
                waitingTimeLines.add(new WaitingTimeLine(line));
                break;
            case 'D':
                apply(new QueryLine(line));
                break;
            default:
                throw new IllegalArgumentException("Illegal line's first symbol. Symbol 'C' or 'D' is required. " + line);
        }
    }

    void apply(QueryLine query) {
        OptionalDouble avgTime = waitingTimeLines.stream().filter((wt)
                -> wt.serviceId.match(query.serviceId)
                && wt.questionTypeId.match(query.questionTypeId)
                && wt.firstAnswer == query.firstAnswer
                && wt.date.compareTo(query.dateFrom) >= 0
                && wt.date.before(query.dateTo))
                .mapToInt((wt) -> wt.waitingTime).average();

        String queryResult;

        if (avgTime.isPresent()) {
            queryResult = String.format("%.0f", avgTime.getAsDouble());
        } else {
            queryResult = "-";
        }

        output.add(queryResult);
    }

    void print() {
        output.stream().forEach(System.out::println);
    }

    void print(Path path) throws IOException {
        Files.write(path, output);
    }

    String getLastQueryResult() {
        return output.size() > 0 ? output.get(output.size()-1) : "";
    }
}
