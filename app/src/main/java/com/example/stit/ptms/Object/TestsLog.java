package com.example.stit.ptms.Object;

public class TestsLog {
    String Date,Time,Duration;
    int Correct,id;

    public TestsLog(String date, String time, String duration, int correct,int id) {
        this.Date = date;
        this.Time = time;
        this.Duration = duration;
        this.Correct = correct;
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public int getCorrect() {
        return Correct;
    }

    public void setCorrect(int correct) {
        Correct = correct;
    }
}
