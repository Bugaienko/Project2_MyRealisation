package interfaces;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import model.Currency;
import model.User;
import validators.exceptions.ExchangeDataError;

public interface IS_ExchangeService {

    boolean exchangeCurrency(User user, Currency currencySell, Currency currencyBuy, double amount) throws ExchangeDataError;
    boolean deposit(User user, Currency currency, double amount);


}
