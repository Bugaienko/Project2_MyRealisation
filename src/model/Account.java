package model;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final int id;
    private double balance;
    private Currency currency;
    private final User user;


    public Account(int id, Currency currency, User user) {
        this.id = id;
        this.currency = currency;
        this.user = user;

    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }




    public String info() {
        return String.format("%s (%s): balance %.2f;" ,
                currency.getCode(), currency.getTitle(), balance );
    }
//    public String info() {
//        return String.format("%s (%s): balance %.2f; transactions: %d" ,
//                currency.getCode(), currency.getTitle(), balance, history.size() );
//    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", currency=" + currency +
                '}';
    }
}
