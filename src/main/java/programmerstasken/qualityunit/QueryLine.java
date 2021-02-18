package programmerstasken.qualityunit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class QueryLine {

    ServiceId serviceId;
    QuestionTypeId questionTypeId;
    boolean firstAnswer;
    LocalDate dateFrom;       // inclusive
    LocalDate dateTo;         // exclusive    

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
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

        dateFrom = LocalDate.parse(dateValues[0],dateFormatter);  
        
        if (dateValues.length == 2) {
            dateTo = LocalDate.parse(dateValues[1], dateFormatter);
        } else {
            dateTo = LocalDate.MAX;
        }
    }

    @Override
    public String toString() {
        return String.format("D %s %s %s %s%s",
                serviceId,
                questionTypeId,
                firstAnswer ? "P" : "N",
                dateFormatter.format(dateFrom),
                (dateTo.equals(LocalDate.MAX) ? "" : "-" + dateFormatter.format(dateTo)));
    }
}
