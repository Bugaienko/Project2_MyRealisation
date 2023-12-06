package service;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import interfaces.IS_ExchangeService;
import model.*;
import validators.exceptions.ExchangeDataError;

import java.util.Optional;
import java.util.Scanner;

public class ExchangeService implements IS_ExchangeService {

    private final UserService userService;
    private final CurrencyService currencyService;


    public ExchangeService(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
    }

    @Override
    public boolean exchangeCurrency(User user, Currency currencySell, Currency currencyBuy, double amount, boolean isConfirmed) throws ExchangeDataError {
        Scanner scanner = new Scanner(System.in);

        /*
        3. Проверить есть ли счет в валюте Sell
        4. Проверить есть ли счет в валюте Buy (если нет - создать)
        5. Достаточно ли на счету средств?
        6. Провести обмен
        7. Записать истории
         */

        if (currencySell == null || currencyBuy == null) throw new ExchangeDataError("С такой валютой не работаем");

        Optional<Account> accountOptionalSell = currencyService.getAccountByCurrency(user, currencySell);
        if (accountOptionalSell.isEmpty()) throw new ExchangeDataError("У вас нет счета в " + currencySell.getCode());
        Account accountSell = accountOptionalSell.get();

        Account accountBuy = getOrCreateAccount(user, currencyBuy);


        if (accountSell.getBalance() < amount)
            throw new ExchangeDataError("На счету недостаточно средств для операции обмена");

        // вычисление курса и суммы к списанию / зачислению на счет
        double rateSell = currencyService.getCurrencyRate(currencySell);
        double rateBuy = currencyService.getCurrencyRate(currencyBuy);
        double crossCourse = rateSell / rateBuy;

        double amountBuy = amount * crossCourse;

        System.out.printf("Обмен %.2f %s на %.2f %s. Кросс-курс: %.5f\n",
                amount, currencySell.getTitle(), amountBuy, currencyBuy.getCode(), crossCourse);


        if (isConfirmed) {
            System.out.println("Для подтверждения операции введете Y или y");
            String confirm = scanner.nextLine();
            if (!"y".equalsIgnoreCase(confirm.trim())) {
                System.out.println("Операция прервана");
                return false;
            }
        }
        System.out.println("Производится обмен (скрестите пальцы!)");

        Operation operationSell = currencyService.createOperation(
                user, TypeOperation.SELL, amount, currencySell, rateSell);
        Operation operationBuy = currencyService.createOperation(
                user, TypeOperation.BUY, amountBuy, currencyBuy, rateBuy);

        //изменение счетов и запись историй
        boolean isSold = currencyService.accountApplyOperation(accountSell, operationSell);
        boolean isBought = false;
        if (isSold) {
            isBought = currencyService.accountApplyOperation(accountBuy, operationBuy);
        }

        if (!isBought) throw new ExchangeDataError("Error! Операция обмена не состоялась!");

        System.out.printf("Успех. Произведен %.2f %s обмен на %.2f %s (кросс-курс: %.5f)\n", amount, currencySell.getCode(), amountBuy, currencyBuy.getCode(), crossCourse);

        return true;
    }

    @Override
    public boolean deposit(User user, Currency currency, double amount) {
         /*
        3. Проверить есть ли счет в валюте Deposit (если нет - создать)
        4. Провести депозит
        5. Записать истории
         */

        Account accountDeposit = getOrCreateAccount(user, currency);


        Operation operationDeposit = currencyService.createOperation(user, TypeOperation.DEPOSIT, amount, currency, 1);

        boolean saved = currencyService.accountApplyOperation(accountDeposit, operationDeposit);

        if (!saved) {
            System.out.println("Ошибка! Внести депозит не удалось");
            return false;
        }

        System.out.printf("Успех! На счет внесено %.2f %s\n", amount, currency.getCode());
        return true;

    }

    private Account getOrCreateAccount(User user, Currency currency) {
        Optional<Account> accountOptionalDeposit = currencyService.getAccountByCurrency(user, currency);

        if (accountOptionalDeposit.isEmpty()) {
            System.out.println("У вас нет счета в такой валюте");
            System.out.println("Открываем счет для работы с " + currency.getCode());
            return currencyService.createAccount(user, currency);
        } else {
            return accountOptionalDeposit.get();
        }
    }
}
