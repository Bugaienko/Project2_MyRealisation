package view;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import model.*;
import service.CurrencyService;
import service.ExchangeService;
import service.UserService;
import validators.exceptions.EmailValidateException;
import validators.exceptions.ExchangeDataError;
import validators.exceptions.PasswordValidateException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {

    private final UserService userService;
    private final CurrencyService currencyService;
    private final ExchangeService exchangeService;

    private final static Scanner SCANNER = new Scanner(System.in);
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");


    public Menu(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.exchangeService = new ExchangeService(userService, currencyService);
    }

    public void run() {
        showMenu();
    }

    private void showMenu() {
        while (true) {
            System.out.println("Добро пожаловать в обмен валют");
            System.out.println("=========== v 1.0 ===========");
            System.out.println("1. Обмен валют");
            System.out.println("2. Меню пользователей");
            System.out.println("3. Меню администратора");
            System.out.println("0. Выход");
            System.out.println("\nСделайте выбор:");
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            if (choice == 0) {
                System.out.println("До свидания");
                break;
            }
            showSubMenu(choice);

        }
    }

    private void showSubMenu(int choice) {
        switch (choice) {
            case 1:
                showCurrencyMenu();
                break;
            case 2:
                showUsersMenu();
                break;
            case 3:
                //TODO admin menu
                //showAdminMenu();
                break;
            default:
                System.out.println("Ваш выбор не корректен");
        }
    }

    private void showCurrencyMenu() {
        while (true) {
            System.out.println("Currency Menu");
            System.out.println("1. Список валют");
            System.out.println("2. Текущие курсы");
            System.out.println("3. Обмен валют");
            System.out.println("4. История курсов");
            System.out.println("5. Пополнить счет");
            System.out.println("6. Снять со счета");
            System.out.println("0. Вернуться в предыдущее меню");
            System.out.println("\nСделайте выбор:");
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            if (choice == 0) break;
            choiceCurrencyMenuProcessing(choice);
        }
    }

    private void showUsersMenu() {
        while (true) {
            System.out.println("Users Menu");
            System.out.println("1. Авторизоваться");
            System.out.println("2. Регистрация пользователя");
            System.out.println("3. Список счетов пользователя");
            System.out.println("4. Добавить счет");
            System.out.println("5. Удалить счет");
            System.out.println("6. История всех операций");
            System.out.println("7. История операций по валюте");
            System.out.println("0. Вернуться в предыдущее меню");
            System.out.println("\nСделайте выбор:");
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            if (choice == 0) break;
            choiceUserMenuProcessing(choice);
        }
    }

    private void choiceCurrencyMenuProcessing(int choice) {
        switch (choice) {
            case 1:
                menuCurrencyList();

                waitRead();
                break;
            case 2:
                menuCurrencyRatesList();

                waitRead();
                break;
            case 3:
                menuExchange();

                waitRead();
                break;
            case 4:
                //TODO menuRatesHistory();
                System.out.println("Не реализован!");

                waitRead();
                break;
            case 5:
                menuDepositAccount();

                waitRead();
                break;
            case 6:
                //TODO menuDebitAccount();

                waitRead();
                break;

            case 0:

                break;
            default:
                System.out.println("Не верный ввод\n");
                waitRead();
        }
    }

    private void menuDepositAccount() {
        /*
        1. Получить два кода валюты
        2. Получить сумму
        3. Отправить в сервис на обработку
         */

        System.out.println("Операция пополнения счета");
        User activeUser = userService.getActiveUser();
        if (activeUser == null) {
            System.out.println("Авторизуйтесь в системе");
            return;
        }
        System.out.println("Выберите валюту для пополнения счета");
        Currency currencyDeposit = getCurrencyByInputCode();

        System.out.println("Введите сумму в " + currencyDeposit.getCode() + ", которую вы положить на счет:");
        double amount = SCANNER.nextDouble();
        SCANNER.nextLine();

        exchangeService.deposit(activeUser, currencyDeposit, amount);
    }

    private void menuExchange() {
         /*
        1. Получить два кода валюты
        2. Получить сумму для конвертации
        3. Отправить в сервис на обработку
         */

        System.out.println("Операция обмена");
        User activeUser = userService.getActiveUser();
        if (activeUser == null) {
            System.out.println("Авторизуйтесь в системе");
            return;
        }
        System.out.println("Выберите валюту, которую вы хотите обменять");
        Currency currencySell = getCurrencyByInputCode();


        System.out.println("Введите валюту, которую Вы хотите получить");
        Currency currencyBuy = getCurrencyByInputCode();


        System.out.println("Введите сумму в " + currencySell.getCode() + ", которую вы хотите обменять:");
        double amount = SCANNER.nextDouble();
        SCANNER.nextLine();

        try {
            exchangeService.exchangeCurrency(activeUser, currencySell, currencyBuy, amount);
        } catch (ExchangeDataError e) {
            System.out.println("Введены некорректные данные");
            System.out.println(e.getMessage());
        }
    }

    private void menuCurrencyRatesList() {
        System.out.println("Текущие курсы валют:");
        Map<String, Rate> rates = currencyService.getRates();
        for (Map.Entry<String, Rate> rateEntry : rates.entrySet()) {
            System.out.printf("1 %s = %.6f EUR\n", rateEntry.getKey(), rateEntry.getValue().getRate());
        }
    }

    private void menuCurrencyList() {
        System.out.println("Список всех доступных валют:");
        List<Currency> currencies = currencyService.getAllCurrencies();
        for (Currency currency : currencies) {
            System.out.printf("%s: %s\n", currency.getCode(), currency.getTitle());
        }
    }

    private Currency getCurrencyByInputCode() {
        System.out.println("Введите код валюты:");
        String inputCur = SCANNER.nextLine().trim().toUpperCase();
        return currencyService.getCurrencyByCode(inputCur.trim().toUpperCase());
    }

    private void choiceUserMenuProcessing(int choice) {
        switch (choice) {
            case 1:
                menuUserAuthorisation();

                waitRead();
                break;
            case 2:
                menuUserRegistration();

                waitRead();
                break;
            case 3:
                showUsersAccounts();

                waitRead();
                break;
            case 4:
                //TODO menuAddAccount();

                waitRead();
                break;
            case 5:
                //TODO menuDeleteAccount();

                waitRead();
                break;
            case 6:
                menuUserHistory();
                waitRead();
                break;
            case 7:
                menuUserHistoryByCurrency();
                waitRead();
                break;
            case 0:

                break;
            default:
                System.out.println("Не верный ввод\n");
                waitRead();
        }
    }



    private void menuUserRegistration() {
        System.out.println("Регистрация пользователя");
        System.out.println("Введите email");
        String email = SCANNER.nextLine();
        System.out.println("Пароль должен быть не менее 8 символов. Должен содержать как минимум: цифру, спец.символ, букву в верхнем и нижнем регистрах");
        System.out.println("Введите пароль: ");
        String password = SCANNER.nextLine();
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = userService.createUser(email, password);
        } catch (EmailValidateException e) {
            System.out.println("Пользователь не зарегистрирован");
            System.out.println(e.getMessage());
            return;
        } catch (PasswordValidateException e) {
            System.out.println("Пользователь не зарегистрирован");
            System.out.println(e.getMessage());
            return;
        }
        if (optionalUser.isEmpty()) {
            System.out.println("Пользователь не зарегистрирован");
            System.out.println("Пользователь с таким email существует");
            return;
        }
        User userReg = optionalUser.get();
        System.out.printf("Пользователь с email %s успешно зарегистрирован\n", userReg.getEmail());

    }

    private void menuUserHistory() {
        System.out.println("История всех операций");
        User activeUser = userService.getActiveUser();
        if (activeUser == null) {
            System.out.println("Авторизуйтесь в системе");
            return;
        }

        System.out.println("Пользователь: " + activeUser);
        List<Operation> operations = currencyService.getOperationsHistory(activeUser);
        showOperations(operations);
    }

    private void menuUserHistoryByCurrency() {

        System.out.println("История всех операций");
        User activeUser = userService.getActiveUser();
        if (activeUser == null) {
            System.out.println("Авторизуйтесь в системе");
            return;
        }

        Currency currency = getCurrencyByInputCode();
        if (currency == null) {
            System.out.println("С такой валютой не работаем");
            return;
        }

        System.out.println("Пользователь: " + activeUser);
        List<Operation> operations = currencyService.getOperationsHistory(activeUser).stream()
                .filter(o -> o.getCurrency().equals(currency)).collect(Collectors.toList());
        showOperations(operations, currency);
    }

    private void showOperations(List<Operation> operations) {
        if (operations.isEmpty()) {
            System.out.println("У Вас еще не было завершенных операций");
            return;
        }
       printOperations(operations);
    }

    private void printOperations(List<Operation> operations) {
               System.out.println("Список операций:");
        for (Operation operation : operations){
            System.out.printf("id:%d; %s: %s; amount: %.2f (rate: %.5f); date: %s\n",
                    operation.getOperationId(), operation.getCurrency().getCode(), operation.getType(),
                    operation.getAmount(), operation.getRate() ,operation.getTime().format(formatter));
        }
    }

    private void showOperations(List<Operation> operations, Currency currency) {
        if (operations.isEmpty()) {
            System.out.println("У Вас еще не было завершенных операций в " + currency.getCode());
            return;
        }

        printOperations(operations);
    }

    private void showUsersAccounts() {
        System.out.println("Список счетов текущего пользователя");
        User activeUser = userService.getActiveUser();
        if (activeUser != null) {
            List<Account> userAccounts = currencyService.getUserAccounts(activeUser);
            menuPrintAccounts(userAccounts);
        } else {
            System.out.println("Авторизуйтесь в системе");
        }
    }

    private void menuPrintAccounts(List<Account> userAccounts) {
        if (userAccounts.isEmpty()) {
            System.out.println("У вас нет открытых счетов");
        } else {
            System.out.printf("Активных счетов: %d\n", userAccounts.size());
            for (Account account : userAccounts) {
                int countTransaction = currencyService.getAccountHistory(account).size();
                System.out.println(account.info() + " Transactions: " + countTransaction);
            }
        }
    }

    private void menuUserAuthorisation() {
        System.out.println("Авторизация пользователя");
        System.out.println("Введите email");
        String emailAu = SCANNER.nextLine();
        System.out.println("Введите пароль: ");
        String passwordAu = SCANNER.nextLine();
        User user = userService.authorisation(emailAu, passwordAu);
        if (user != null) {
            System.out.printf("%s успешно авторизирован\n", user);
        } else {
            System.out.println("Авторизация провалена");
        }
    }

    private void waitRead() {
        System.out.println("\nДля продолжения нажмите Enter ...");
        SCANNER.nextLine();
    }


}
