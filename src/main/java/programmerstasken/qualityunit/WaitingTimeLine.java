package programmerstasken.qualityunit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class WaitingTimeLine {

    ServiceId serviceId;
    QuestionTypeId questionTypeId;

    boolean firstAnswer;
    Date date;
    int waitingTime;           // in minutes

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Waiting Timeline syntax: 
     * "C service_id[.variation_id] question_type_id[.category_id.[sub-category_id]] P/N date time" 
     * Timeline example: "C 1 10.1 P 01.12.2012 65"
     */
    WaitingTimeLine(String waitingTimeLine) {
        String values[] = waitingTimeLine.split(" ");

        if (values.length != 6) {
            throw new IllegalArgumentException(waitingTimeLine);
        }

        serviceId = new ServiceId(values[1]);
        questionTypeId = new QuestionTypeId(values[2]);
        firstAnswer = values[3].equals("P");

        try {
            date = dateFormat.parse(values[4]);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Invalid date format: " + waitingTimeLine);
        }

        waitingTime = Integer.valueOf(values[5]);
    }

    @Override
    public String toString() {
        return String.format("C %s %s %s %s %d",
                serviceId,
                questionTypeId,
                firstAnswer ? "P" : "N",
                dateFormat.format(date),
                waitingTime);
    }
}
