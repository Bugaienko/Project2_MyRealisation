package service;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import interfaces.IS_CurrencyService;
import model.*;
import repository.AccountRepository;
import repository.CurrencyRepository;
import repository.OperationRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CurrencyService implements IS_CurrencyService {
    private final CurrencyRepository currencyRepo;
    private final AccountRepository accountRepo;

    private final OperationRepository operationRepo;

    public CurrencyService(CurrencyRepository currencyRepo, AccountRepository accountRepo, OperationRepository operationRepo) {
        this.currencyRepo = currencyRepo;
        this.accountRepo = accountRepo;
        this.operationRepo = operationRepo;
    }

    @Override
    public Currency getCurrencyByCode(String curCode) {
        return currencyRepo.getCurrencyByCode(curCode);
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepo.getAllCurrencies();
    }

    @Override
    public Map<String, Rate> getRates() {
        return currencyRepo.getRates();
    }

    @Override
    public List<Rate> getHistory(Currency currency) {
        return currencyRepo.getRateHistory(currency);
    }

    @Override
    public double getCurrencyRate(Currency currency) {
        return currencyRepo.getCurrencyRate(currency).getRate();
    }

    @Override
    public List<Rate> getRateHistory(Currency currency) {
        return currencyRepo.getRateHistory(currency);
    }

    @Override
    public Account createAccount(User user, Currency currency) {
        return accountRepo.createAccount(user, currency);
    }

    @Override
    public List<Account> getUserAccounts(User user) {
        return accountRepo.getAllUsersAccounts(user);
    }

    @Override
    public boolean deleteAccount(User user, Currency currency) {
        List<Account> userAccounts = accountRepo.getAllUsersAccounts(user);
        if (userAccounts.isEmpty()) {
            System.out.println("У вас нет активных счетов");
            return false;
        }


        Optional<Account> accountOptional = accountRepo.getUserAccountByCurrency(user, currency);
        if (accountOptional.isEmpty()) {
            System.out.println("У вас нет счета в такой валюте");
            return false;
        }

        Account account = accountOptional.get();
        if (account.getBalance() > 0) {
            System.out.printf("На счету %.2f %s\n", account.getBalance(), account.getCurrency().getCode());
            System.out.println("Вы не можете закрыть не счет c ненулевым остатком");
            return false;
        }

        return accountRepo.deleteAccount(user, account);
    }

    @Override
    public Optional<Account> getAccountByCurrency(User user, Currency currency) {
        return accountRepo.getUserAccountByCurrency(user, currency);
    }

    @Override
    public boolean accountApplyOperation(Account account, Operation operation) {

        operationRepo.saveOperation(operation);
        return accountRepo.applyOperation(account, operation);

    }

    @Override
    public Operation createOperation(User user, TypeOperation typeOperation, double amount, Currency currency, double rate) {
        return operationRepo.createOperation(user, typeOperation, amount, currency, rate);
    }


    @Override
    public List<Operation> getOperationsHistory(User user) {
       return operationRepo.getUserOperations(user);
    }

    public List<Operation> getAccountHistory(Account account){
        return accountRepo.getHistory(account);
    }

    @Override
    public boolean isAccountExist(User user, Currency currency) {
        return accountRepo.isAccountExist(user, currency);
    }

    @Override
    public Currency createCurrency(String code, String title, double rate) {
        return currencyRepo.createNewCurrency(code, title, rate);
    }

    @Override
    public List<Operation> getAllOperationByCurrency(Currency currency) {
        return operationRepo.getOperationsByCurrency(currency);
    }

    @Override
    public void setCurrencyRate(Currency currency, double rate) {
        currencyRepo.setRate(currency, rate);
    }
}
