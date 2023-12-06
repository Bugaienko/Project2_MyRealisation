package service;
/*
@date 05.12.2023
@author Sergey Bugaienko
*/

import model.Account;
import model.Currency;
import model.Operation;
import model.User;
import validators.exceptions.AdminRequestDataError;
import validators.exceptions.ExchangeDataError;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminService {

    private final UserService userService;
    private final CurrencyService currencyService;
    private final ExchangeService exchangeService;

    public AdminService(UserService userService, CurrencyService currencyService, ExchangeService exchangeService) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.exchangeService = exchangeService;
    }

    public Currency createNewCurrency(String code, String title, double rate) throws AdminRequestDataError {
        if (code.length() != 3) throw new AdminRequestDataError("Код валюты должен содержать ровно 3 буквы");
        if (title.isEmpty()) throw new AdminRequestDataError("Название валюты не должно быть пустым");
        if (rate <= 0) throw new AdminRequestDataError("Не верный курс валюты " + code);

        Currency currencyOptional = currencyService.getCurrencyByCode(code);
        if (currencyOptional != null)
            throw new AdminRequestDataError(String.format("Такая валюта (%s) уже существует в системе ", code));

        return currencyService.createCurrency(code, title, rate);
    }

    public List<Operation> getUsersOperations(String userIdentification) throws AdminRequestDataError {
        String regEmail = "^[_A-Za-z0-9-\\\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Optional<User> userOptional = Optional.empty();

        if (userIdentification.matches(regEmail)) {
            System.out.println("Ищу пользователя по E-mail...");
            userOptional = userService.getUserByEmail(userIdentification);
        } else {
            try {
                int id = Integer.parseInt(userIdentification);
                System.out.println("Ищу пользователя по id");
                userOptional = userService.getUserById(id);
            } catch (NumberFormatException e) {
                throw new AdminRequestDataError("Неверные данные для поиска пользователя '" + userIdentification + "'");
            }
        }

        if (userOptional.isEmpty())
            throw new AdminRequestDataError("Пользователя по запросу '" + userIdentification + "' не найден");

        User user = userOptional.get();
        System.out.println("Найден пользователь: " + user);
        return currencyService.getOperationsHistory(user);
    }

    public List<Operation> getCurrencyOperationsList(Currency currency) {
        return currencyService.getAllOperationByCurrency(currency);
    }

    public void changeCurrencyRate(Currency currency, double rate) throws AdminRequestDataError {
        if (rate <= 0) throw new AdminRequestDataError("Не верный курс валюты " + rate);
        double currentRate = currencyService.getCurrencyRate(currency);
        if (currentRate == rate) throw new AdminRequestDataError("У валюты текущий курс равен устанавливаемому");
        if (currency.getCode().equalsIgnoreCase("EUR")) throw new AdminRequestDataError("Курс Евро менять нельзя!");

        currencyService.setCurrencyRate(currency, rate);

    }

    public void deleteCurrency(Currency currency) throws AdminRequestDataError {
        if (currency.getCode().equalsIgnoreCase("EUR")) throw new AdminRequestDataError("EUR удалять нельзя");
        List<User> users = currencyService.getAllUsersWithAccountInCurrency(currency);
//        List<Account> accounts = currencyService.getAllAccountsByCurrency(currency);
//     accounts = accounts.stream().filter(account -> account.getBalance() > 0).collect(Collectors.toList());
//        if (!accounts.isEmpty()) {
        for (User user : users) {
            checkAndChangeDeletingCurrency(user, currency);
        }
//        } else {
//            //Todo safe delete
//        }
        System.out.println("Все счета закрыты");

        currencyService.deleteCurrencyFromDB(currency);
        System.out.println("Валюта удалена из списка доступных");

    }

    private void checkAndChangeDeletingCurrency(User user, Currency currency) {
        Currency eur = currencyService.getCurrencyByCode("EUR");
        Optional<Account> accountForDelete = currencyService.getAccountByCurrency(user, currency);
        Account accountDel = null;
        if (accountForDelete.isPresent()) accountDel = accountForDelete.get();
        double balance = accountDel.getBalance();
        if (balance > 0) {
            try {
                boolean isChanged = exchangeService.exchangeCurrency(user, currency, eur, balance, false);
                if (isChanged) System.out.println("Обмен удаляемой валюты проведен успешно!");
            } catch (ExchangeDataError e) {
                System.out.println(e.getMessage());
            }
        }
        currencyService.deleteAccount(user, currency);
    }
}
