package validators.exceptions;
/*
@date 05.12.2023
@author Sergey Bugaienko
*/

public class AdminRequestDataError extends Exception{
    public AdminRequestDataError(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Ошибка ввода данных администратором | " + super.getMessage();
    }
}
