package com.example.stit.ptms.Object;

public class Result {
    String ans,question;
    boolean correct;

    public Result(String ans, String question, boolean correct) {
        this.ans = ans;
        this.question = question;
        this.correct = correct;
    }

    public Result() {

    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
