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
    public boolean deleteAccount(User user, Account account) {
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
    public boolean saveOperation(Operation operation) {
        //Todo
        return false;
    }

    @Override
    public boolean saveHistory(Operation operation) {
        //Todo
        return false;
    }

    @Override
    public boolean saveHistory(List<Operation> operations) {
        //Todo
        return false;
    }

    @Override
    public List<Operation> getOperationsHistory(User user) {
       return operationRepo.getUserOperations(user);
    }

    public List<Operation> getAccountHistory(Account account){
        return accountRepo.getHistory(account);
    }
}
