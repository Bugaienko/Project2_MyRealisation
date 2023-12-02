/*
@date 30.11.2023
@author Sergey Bugaienko
*/

import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.AccountRepository;
import repository.CurrencyRepository;
import repository.OperationRepository;
import repository.UserRepository;
import service.CurrencyService;
import service.ExchangeService;
import service.UserService;

public class CurrencyTests {

    private User activeUser;

    private final CurrencyService currencyService;
    private final UserService userService;
    private final ExchangeService exchangeService;

    public CurrencyTests() {
        this.currencyService = new CurrencyService(new CurrencyRepository(), new AccountRepository(), new OperationRepository());
        this.userService = new UserService(new UserRepository());
        this.exchangeService = new ExchangeService(userService, currencyService);
    }

    @BeforeEach
    void init(){

    }

    @Test
    public void test1(){

    }
}
