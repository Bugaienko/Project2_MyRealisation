package model;
/*
@date 25.11.2023
@author Sergey Bugaienko
*/

import java.time.LocalDateTime;

public class Rate {
    private double rate;
    private final LocalDateTime time;

    public Rate(double rate) {
        this.rate = rate;
        this.time = LocalDateTime.now();
    }


    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "{" +
                rate +
                " (" + time +
                ")}";
    }
}
