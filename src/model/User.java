package model;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final int id; // идентификатор
    private String email; // идентификатор?
    private String password;
    private UserRole role;
//    private final List<Operation> operations; // надо ли здесь хранить? скорее нет, чем да

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
//        this.operations = new ArrayList<>();
        this.role = UserRole.CLIENT;
        //this.accounts = new ArrayList<>();

    }

    //TODO email and password validator


    public UserRole getRole() {
        return role;
    }

    //TODO только админ
    public void setRole(UserRole role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

//    public List<Operation> getOperations() {
//        return operations;
//    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
