package service;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import interfaces.IS_UserService;
import model.User;
import repository.UserRepository;
import validators.EmailValidator;
import validators.PasswordValidator;
import validators.exceptions.EmailValidateException;
import validators.exceptions.PasswordValidateException;

import java.util.Optional;

public class UserService implements IS_UserService {

    private User activeUser;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> createUser(String email, String password) throws PasswordValidateException, EmailValidateException {
        EmailValidator.validate(email);
        PasswordValidator.validate(password);
        User user = null;
        if (!userRepository.isEmailExist(email)) {
            user = userRepository.addUser(email, password);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User authorisation(String email, String password) {
        logout();
        Optional<User> optionalUser = userRepository.authorisation(email, password);
        optionalUser.ifPresent(user -> activeUser = user);

        return activeUser;
    }

    @Override
    public void logout() {
        activeUser = null;
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }
}
