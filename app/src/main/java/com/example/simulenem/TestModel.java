package com.example.simulenem;

public class TestModel {

    private String test_id;
    private int topScore;
    private int time;

    public TestModel(String test_id, int topScore, int time) {
        this.test_id = test_id;
        this.topScore = topScore;
        this.time = time;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
