package com.example.simulenem;

public class QuestionModel {
    private String question;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String option_e;
    private int correct_ans;
    private int selected_ans;

    public QuestionModel(String question,
                         String option_a, String option_b,String option_c, String option_d, String option_e,
                         int correct_ans, int selected_ans) {
        this.question = question;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.option_e = option_e;
        this.correct_ans = correct_ans;
        this.selected_ans = selected_ans;
    }

    public int getSelected_ans() {
        return selected_ans;
    }

    public void setSelected_ans(int selected_ans) {
        this.selected_ans = selected_ans;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public String getOption_e() {
        return option_e;
    }

    public void setOption_e(String option_e) {
        this.option_e = option_e;
    }

    public int getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(int correct_ans) {
        this.correct_ans = correct_ans;
    }
}
