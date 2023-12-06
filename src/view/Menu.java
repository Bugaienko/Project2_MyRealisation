package view;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import model.*;
import model.Currency;
import service.AdminService;
import service.CurrencyService;
import service.ExchangeService;
import service.UserService;
import validators.exceptions.AdminRequestDataError;
import validators.exceptions.EmailValidateException;
import validators.exceptions.ExchangeDataError;
import validators.exceptions.PasswordValidateException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Menu {

    private final UserService userService;
    private final CurrencyService currencyService;
    private final ExchangeService exchangeService;
    private final AdminService adminService;

    private final static Scanner SCANNER = new Scanner(System.in).useLocale(Locale.US);
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    public Menu(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.exchangeService = new ExchangeService(userService, currencyService);
        this.adminService = new AdminService(userService, currencyService, exchangeService);
    }

    public void run() {
        showMenu();
    }

    private void showMenu() {
        while (true) {
            boolean isLogged = userService.getActiveUser() != null;
            System.out.println("Добро пожаловать в обмен валют");
            System.out.println("=========== v 1.0 ===========");
            System.out.println("1. Меню пользователей");
            if (isLogged) {
                System.out.println("2. Обмен валют");
                System.out.println("3. Меню администратора");
            }
            System.out.println("0. Выход");
            if (!isLogged) {
                System.out.println("Авторизуйтесь в системе для доступа к другим меню");
            }
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
            case 2:
                showCurrencyMenu();
                break;
            case 1:
                showUsersMenu();
                break;
            case 3:
                showAdminMenu();
                break;
            default:
                System.out.println("Ваш выбор не корректен");
        }
    }

    /*
    - Изменение курса валюты
- Возможность добавление или удаление валют из списка (При удалении должна быть проверка, есть ли открытые счета у пользователей в этой валюте? Если есть - что делать?)
- Просмотр операций пользователя
- Просмотр операций по валюте
- Назначение другого пользователя администратором (модератором / кассиром)
     */
    private void showAdminMenu() {
        User activeUser = userService.getActiveUser();
        if (activeUser == null) {
            System.err.println("Доступ запрещен! Вы не авторизированны в системе.");
            waitRead();
            return;
        }

        if (activeUser.getRole() != UserRole.ADMIN) {
            System.err.println("Доступ запрещен! У вас нет прав администратора");

            waitRead();
            return;
        }
        while (true) {
            System.out.println("Admin Menu");
            System.out.println("1. Список валют");
            System.out.println("2. Текущие курсы");
            System.out.println("3. Изменение курса валюты");
            System.out.println("4. Добавление новой валюты");
            System.out.println("5. Удаление валюты");
            System.out.println("6. Просмотр операций пользователя");
            System.out.println("7. Просмотр всех операций по валюте");
            System.out.println("8. Список пользователей");
            System.out.println("9. Назначить пользователя администратором");
            System.out.println("0. Выход из меню");
            System.out.println("\nСделайте выбор:");
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            if (choice == 0) break;
            choiceAdminMenuProcessing(choice);
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
            System.out.println("7. Остатки на счетах");
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
            boolean isLogged = userService.getActiveUser() != null;
            System.out.println("Users Menu");
            System.out.println("1. Авторизоваться");
            System.out.println("2. Регистрация пользователя");
            if (isLogged) {
                System.out.println("3. Список счетов пользователя");
                System.out.println("4. Добавить счет");
                System.out.println("5. Удалить счет");
                System.out.println("6. История всех операций");
                System.out.println("7. История операций по валюте");
            }
            System.out.println("0. Вернуться в предыдущее меню");
            if (!isLogged) {
                System.out.println("Авторизуйтесь в системе для доступа к другим меню");
            }
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
                menuRatesHistory();

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
            case 7:
                showUsersAccounts();

                waitRead();
                break;
            case 0:

                break;
            default:
                System.out.println("Не верный ввод\n");
                waitRead();
        }
    }

    /*
    1. Список валют"
2. Текущие курсы"
3. Изменение курса валюты"
4. Добавление новой валюты"
5. Удаление валюты"
6. Просмотр операций пользователя"
7. Просмотр всех операций по валюте"
8. Список пользователей"
9. Назначить пользователя администратором"
0. Выход из меню"
     */
    private void choiceAdminMenuProcessing(int choice) {
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
                adminChangeCurrencyRate();

                waitRead();
                break;
            case 4:
                adminAddCurrency();

                waitRead();
                break;
            case 5:
                adminDeleteCurrency();

                waitRead();
                break;
            case 6:
                adminShowUserOperations();

                waitRead();
                break;
            case 7:
                adminShowCurrencyOperations();

                waitRead();
                break;
            case 8:
                //TODO adminShowAllUsers();

                waitRead();
                break;
            case 9:
                //TODO adminMakeAdmin();

                waitRead();
                break;
            case 0:

                break;
            default:
                System.out.println("Не верный ввод\n");
                waitRead();
        }
    }

    private void adminDeleteCurrency() {
        System.out.println("Удаление валюты");

        Currency currency = getCurrencyByInputCode();
        if (currency == null) {
            System.out.println("Нет у нас такой валюты");
            return;
        }

        try {
            adminService.deleteCurrency(currency);
        } catch (AdminRequestDataError e) {
            System.out.println(e.getMessage());
            System.err.println("Ошибка!");
        }
    }

    private void adminChangeCurrencyRate() {
        System.out.println("Изменение курса валюты");

        Currency currency = getCurrencyByInputCode();
        if (currency == null) {
            System.out.println("Нет у нас такой валюты. Git push и спать!");
            return;
        }

        System.out.println("Введите текущий курс к ЕВРО новой валюты");
        double rate = SCANNER.nextDouble();
        SCANNER.nextLine();

        try {
            adminService.changeCurrencyRate(currency, rate);
            System.out.println("OK. Курс успешно обновлен");
        } catch (AdminRequestDataError e) {
            System.out.println(e.getMessage());
            System.err.println("Ошибка!");
        }


    }

    private void adminShowCurrencyOperations() {
        Currency currency = getCurrencyByInputCode();
        if (currency == null) {
            System.out.println("Нет у нас такой валюты. Выпей кофе!");
            return;
        }

        List<Operation> operations = adminService.getCurrencyOperationsList(currency);
        printOperations(operations);
    }

    private void adminShowUserOperations() {
        System.out.println("Показ операций пользователя");

        System.out.println("Введите email или id пользователя");
        String userIdentification = SCANNER.nextLine();

        try {
            List<Operation> usersOperations = adminService.getUsersOperations(userIdentification);
            printOperations(usersOperations);
        } catch (AdminRequestDataError e) {
            System.out.println(e.getMessage());
            System.err.println("Ошибка!");
        }
    }

    private void adminAddCurrency() {
        System.out.println("Добавление новой валюты");
        String code, title;
        double rate;
        System.out.println("Введи код новой валюты (3 буквы): ");
        code = SCANNER.nextLine().trim().toUpperCase();

        System.out.println("Введи название новой валюты");
        title = SCANNER.nextLine();

        System.out.println("Введите текущий курс к ЕВРО новой валюты");
        rate = SCANNER.nextDouble();
        SCANNER.nextLine();


        try {
            Currency currency = adminService.createNewCurrency(code, title, rate);
            System.out.printf("Успешно создана валюты %s (%s)\n", currency.getCode(), currency.getTitle());
        } catch (AdminRequestDataError e) {
            System.err.println("Ошибка!");
            System.out.println(e.getMessage());
        }

    }

    private void menuRatesHistory() {
        Currency currency = getCurrencyByInputCode();
        if (currency == null) {
            System.out.println("По этой валюте информации в истории нет");
            return;
        }
        List<Rate> historyRates = currencyService.getHistory(currency);
        if (historyRates.isEmpty()) {
            System.out.printf("История по валюте %s отсутствует\n", currency.getTitle());
            return;
        }
        System.out.printf("История изменения курсов %s (%s):\n", currency.getCode(), currency.getTitle());
        for (Rate rate : historyRates) {
            System.out.printf("%s: %.6f\n", rate.getTime().format(formatter), rate.getRate());
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
            exchangeService.exchangeCurrency(activeUser, currencySell, currencyBuy, amount, true);
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
        String inputCur = SCANNER.nextLine();
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
                menuAddAccount();

                waitRead();
                break;
            case 5:
                menuDeleteAccount();

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

    private void menuDeleteAccount() {
        System.out.println("Удалить счет");
        User activeUser = userService.getActiveUser();
        if (activeUser == null) {
            System.out.println("Авторизуйтесь в системе");
            return;
        }

        Currency currency = getCurrencyByInputCode();
        if (currency == null) {
            System.out.println("У вас нет счета в такой валюте");
            return;
        }

        boolean isClosed = currencyService.deleteAccount(activeUser, currency);

        if (isClosed) {
            System.out.println("Счет успешно закрыт.");
        }

    }

    private void menuAddAccount() {
        System.out.println("Добавить счет");
        User activeUserAdd = userService.getActiveUser();
        if (activeUserAdd == null) {
            System.out.println("Авторизуйтесь в системе");
            return;
        }

        Currency currency = getCurrencyByInputCode();
        if (currency == null) {
            System.out.println("С такой валютой не работаем");
            return;
        }
        System.out.printf("Вы выбрали: %s (%s)\n", currency.getCode(), currency.getTitle());
        if (currencyService.isAccountExist(activeUserAdd, currency)) {
            System.out.printf("У вас уже открыт счет в %s\n", currency.getTitle());
            return;
        }
        currencyService.createAccount(activeUserAdd, currency);
        System.out.printf("Успешно создан счет для валюты %s (%s)\n", currency.getCode(), currency.getTitle());
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
        for (Operation operation : operations) {
            System.out.printf("id:%d; %s: %s; amount: %.2f (rate: %.5f); date: %s\n",
                    operation.getOperationId(), operation.getCurrency().getCode(), operation.getType(),
                    operation.getAmount(), operation.getRate(), operation.getTime().format(formatter));
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
