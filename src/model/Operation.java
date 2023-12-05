package model;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import java.time.LocalDateTime;

public class Operation {
    private final int operationId;
    private final User user;
    private final TypeOperation type;
    private final double amount;
    private final Currency currency;
    private final double rate;
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


    public double getAmount() {
        return amount;
    }


    public Currency getCurrency() {
        return currency;
    }


    public double getRate() {
        return rate;
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
