package model;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import java.time.LocalDateTime;

public class Operation {
    private final int operationId;
    private final User user;
    private TypeOperation type;
    private double amount;
    private Currency currency;
    private double rate;
    private final LocalDateTime time;

    public Operation(int operationId, User user, TypeOperation type, double amount, Currency currency, double rate) {
        this.user = user;
        this.operationId = operationId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.rate = rate;
        time = LocalDateTime.now();
    }

    public int getOperationId() {
        return operationId;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operationId=" + operationId +
                ", type=" + type +
                ", amount=" + amount +
                ", currency=" + currency +
                ", rate=" + rate +
                ", date=" + time +
                '}';
    }
}
