package programmerstasken.qualityunit;

/**
 * The special value "*" stored as a QuestionTypeId where questionType=0
 */
class QuestionTypeId {

    private int questionType;
    private int category;
    private int subCategory;

    QuestionTypeId(String questionTypeId) {
        if (questionTypeId.equals("*")) {
            return;
        }

        String values[] = questionTypeId.split("\\.");

        if (values.length == 0 || values.length > 3) {
            throw new IllegalArgumentException(questionTypeId);
        }

        questionType = Integer.valueOf(values[0]);

        if (values.length >= 2) {
            category = Integer.valueOf(values[1]);
        }

        if (values.length == 3) {
            subCategory = Integer.valueOf(values[2]);
        }
    }

    boolean match(QuestionTypeId matchTo) {
        if (matchTo.questionType == 0) {      //  The special value "*" 
            return true;
        }

        return questionType == matchTo.questionType
                && (category == matchTo.category || matchTo.category == 0)
                && (subCategory == matchTo.subCategory || matchTo.subCategory == 0);
    }

    @Override
    public String toString() {
        if (questionType == 0) {
            return "*";
        }

        if (category == 0) {
            return Integer.toString(questionType);
        }

        if (subCategory == 0) {
            return String.format("%d.%d", questionType, category);
        }

        return String.format("%d.%d.%d", questionType, category, subCategory);
    }
}
