package programmerstasken.qualityunit;

import java.io.IOException;
import java.nio.file.Path;

public class Main {

    /**
     * The program takes its first argument as a path to the input file. If no
     * program arguments specified, then the default filename for input file is
     * "input.txt". The output file is always  "output.txt".
     */
    public static void main(String args[]) throws IOException {
        Path inputFile;

        if (args.length == 0) {
            inputFile = Path.of("input.txt");
        } else {
            inputFile = Path.of(args[0]);
        }

        Path outputFile = Path.of("output.txt");

        Processor waitingTimeLinesProcessor = new Processor();
        waitingTimeLinesProcessor.process(inputFile);
        waitingTimeLinesProcessor.print(outputFile);

        //  waitingTimeLinesProcessor.print();   // prints the result in to the console
    }
}
