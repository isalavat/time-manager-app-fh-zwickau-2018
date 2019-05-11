package de.fh_zwickau.tm.domain;

public class TMQuestion {
    private String category;
    private String question;
    private int answerValue;

    public TMQuestion(String category, String question) {
        this.category = category;
        this.question = question;
        this.answerValue = 1;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(int answerValue) {
        this.answerValue = answerValue;
    }
}
