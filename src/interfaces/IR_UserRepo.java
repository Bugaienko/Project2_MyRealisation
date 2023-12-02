package interfaces;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import model.User;

import java.util.Optional;

public interface IR_UserRepo {
    User addUser(String email, String password);
    User getUserById(int id);
    boolean isEmailExist(String email);
    Optional<User> authorisation(String email, String password);

}
