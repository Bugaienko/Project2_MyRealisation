package interfaces;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IS_CurrencyService {
    Currency getCurrencyByCode(String curCode);
    List<Currency> getAllCurrencies();

    Map<String, Rate> getRates();
    List<Rate> getHistory(Currency currency);
    double getCurrencyRate(Currency currency);
    List<Rate> getRateHistory(Currency currency);


    Account createAccount(User user, Currency currency);
    List<Account> getUserAccounts(User user);
    boolean deleteAccount(User user, Account account);
    Optional<Account> getAccountByCurrency(User user, Currency currency);
    boolean accountApplyOperation(Account account, Operation operation);





    Operation createOperation(User user, TypeOperation typeOperation, double amount, Currency currency, double rate);
    boolean saveOperation(Operation operation); // одно и тоже??
    boolean saveHistory(Operation operation); // одно и тоже??
    boolean saveHistory(List<Operation> operations);

    List<Operation> getOperationsHistory(User user);



}
