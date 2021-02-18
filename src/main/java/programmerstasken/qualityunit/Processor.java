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
    
    public void clear() {
        waitingTimeLines.clear();
        output.clear();
    }

    /**
     * Note: The first line of the file contains the total number of lines.
     */
    public void process(Path path) throws IOException {
        clear();

        try ( Stream<String> lines = Files.lines(path)) {
            lines.skip(1).limit(maxLinesPerFile).forEach(this::process);
        }
    }

    public void process(String line) {
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
                throw new IllegalArgumentException("Illegal the first symbol of the line. Symbol 'C' or 'D' is required. " + line);
        }
    }

    
    private void apply(QueryLine query) {
        OptionalDouble avgTime = waitingTimeLines.stream().filter((wt)
                -> wt.serviceId.match(query.serviceId)
                && wt.questionTypeId.match(query.questionTypeId)
                && wt.firstAnswer == query.firstAnswer
                &&(wt.date.isEqual(query.dateFrom) || wt.date.isAfter(query.dateFrom))
                && wt.date.isBefore(query.dateTo))
                .mapToInt((wt) -> wt.waitingTime).average();

        String queryResult;

        if (avgTime.isPresent()) {
            queryResult = String.format("%.0f", avgTime.getAsDouble());
        } else {
            queryResult = "-";
        }

        output.add(queryResult);
    }
    
    public void print() {
        output.stream().forEach(System.out::println);
    }

    public void print(Path path) throws IOException {
        Files.write(path, output);
    }

    public String getLastQueryResult() {
        return output.size() > 0 ? output.get(output.size()-1) : "";
    }
}
