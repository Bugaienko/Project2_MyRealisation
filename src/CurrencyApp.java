/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import repository.AccountRepository;
import repository.CurrencyRepository;
import repository.OperationRepository;
import repository.UserRepository;
import service.CurrencyService;
import service.UserService;
import view.Menu;

public class CurrencyApp {
    public static void main(String[] args) {
        Menu menu =
                new Menu(
                        new UserService(new UserRepository()),
                        new CurrencyService(new CurrencyRepository(), new AccountRepository(), new OperationRepository())
                );

        menu.run();
    }
}
