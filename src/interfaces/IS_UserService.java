package interfaces;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import model.Currency;
import model.User;
import validators.exceptions.EmailValidateException;
import validators.exceptions.PasswordValidateException;

import java.util.List;
import java.util.Optional;

public interface IS_UserService {
    Optional<User> createUser(String email, String password) throws PasswordValidateException, EmailValidateException;
    User authorisation(String email, String password);
    void logout();
    User getActiveUser();
    List<User> getAllUsers();


    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(int id);
}
