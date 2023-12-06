package repository;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import interfaces.IR_AccountRepo;
import model.*;
import model.Currency;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AccountRepository implements IR_AccountRepo {
    private final AtomicInteger currentAccountId = new AtomicInteger(1);

    //userId
    private final Map<Integer, List<Account>> accounts;
    //accountId
    private final Map<Integer, List<Operation>> accountOperations;

    public AccountRepository() {
        this.accounts = new HashMap<>();
        this.accountOperations = new HashMap<>();
    }

    @Override
    public Account createAccount(User user, Currency currency) {
        Account account = new Account(currentAccountId.getAndIncrement(), currency, user);

        accounts.merge(user.getId(), new ArrayList<>(List.of(account)), (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                }
        );
        return account;
    }



    @Override
    public List<Account> getAllUsersAccounts(User user) {
        return accounts.getOrDefault(user.getId(), new ArrayList<>());
    }

    @Override
    public boolean deleteAccount(User user, Account account) {
        return accounts.getOrDefault(user.getId(), new ArrayList<>()).remove(account);
    }

    @Override
    public Optional<Account> getUserAccountByCurrency(User user, Currency currency) {
        return accounts.getOrDefault(user.getId(), new ArrayList<>()).stream()
                .filter(Objects::nonNull).filter(account -> account.getCurrency().equals(currency)).findFirst();
    }

    @Override
    public boolean applyOperation(Account account, Operation operation) {

        if (!account.getCurrency().equals(operation.getCurrency())) return false;

        double balance = account.getBalance();

        if (operation.getType() == TypeOperation.BUY || operation.getType() == TypeOperation.DEPOSIT) {
            balance += operation.getAmount();
        } else {
            balance -= operation.getAmount();
        }

        if (balance < 0) return false;

        account.setBalance(balance);

        accountOperations.merge(account.getId(), new ArrayList<>(List.of(operation)),
                (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                });

        return true;
    }

    @Override
    public List<Operation> getHistory(Account account) {
        return accountOperations.getOrDefault(account.getId(), new ArrayList<>());
    }

    @Override
    public boolean isAccountExist(User user, Currency currency) {
        return accounts.getOrDefault(user.getId(), new ArrayList<>()).stream()
                .map(Account::getCurrency)
                .anyMatch(c1 -> c1.equals(currency));
    }

    public List<Account> getAllAccounts(){
        return accounts.entrySet().stream().flatMap(entries -> entries.getValue().stream()).collect(Collectors.toList());
    }

    public List<Account> getAllAccountsByCurrency(Currency currency){
        return getAllAccounts().stream().filter(acc -> acc.getCurrency().equals(currency)).collect(Collectors.toList());
    }

    public List<User> getAllUsersWithAccountByCurrency(Currency currency){
         return getAllAccountsByCurrency(currency).stream().map(Account::getUser).collect(Collectors.toList());
    }
}
