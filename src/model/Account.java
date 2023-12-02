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
   // private List<Operation> history;
    // TODO вынести в репозиторий

    public Account(int id, Currency currency, User user) {
        this.id = id;
        this.currency = currency;
        this.user = user;
       // this.history = new ArrayList<>();
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

   // public List<Operation> getHistory() {
       // return history;
    //}

//    public void setHistory(List<Operation> history) {
//        this.history = history;
//    }


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
