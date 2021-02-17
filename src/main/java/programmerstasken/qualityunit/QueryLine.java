package programmerstasken.qualityunit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class QueryLine {

    ServiceId serviceId;
    QuestionTypeId questionTypeId;

    boolean firstAnswer;
    Date dateFrom;       // inclusive
    Date dateTo;         // exclusive    

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final Date maxDate = new Date(Long.MAX_VALUE);

    /**
     * QueryLine syntax: 
     * "D service_id[.variation_id] question_type_id[.category_id[.sub-category_id]] P/N date_from[-date_to]"
     * Examples: 
     * "D 1.1 8 P 01.01.2012-01.12.2012"
     * "D 3 10 P 01.12.2012"
     */
    QueryLine(String queryLine) {

        String values[] = queryLine.split(" ");

        if (values.length != 5) {
            throw new IllegalArgumentException(queryLine);
        }

        serviceId = new ServiceId(values[1]);
        questionTypeId = new QuestionTypeId(values[2]);
        firstAnswer = values[3].equals("P");

        String dateValues[] = values[4].split("-");

        if (dateValues.length == 0 || dateValues.length > 2) {
            throw new IllegalArgumentException(queryLine);
        }

        try {
            dateFrom = dateFormat.parse(dateValues[0]);

            if (dateValues.length == 2) {
                dateTo = dateFormat.parse(dateValues[1]);
            } else {
                dateTo = maxDate;
            }
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Invalid date format: " + queryLine);
        }
    }

    @Override
    public String toString() {
        return String.format("D %s %s %s %s",
                serviceId,
                questionTypeId,
                firstAnswer ? "P" : "N",
                dateFormat.format(dateFrom)
                + (dateTo == maxDate ? "" : "-" + dateFormat.format(dateTo)));
    }
}
