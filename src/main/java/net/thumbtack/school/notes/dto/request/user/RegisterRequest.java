package net.thumbtack.school.notes.dto.request.user;

import net.thumbtack.school.notes.validator.UserLogin;
import net.thumbtack.school.notes.validator.UserName;
import net.thumbtack.school.notes.validator.UserPassword;
import net.thumbtack.school.notes.validator.UserPatronymic;

public class RegisterRequest {

    @UserName
    private String firstName;

    @UserName
    private String lastName;

    @UserPatronymic
    private String patronymic;

    @UserLogin
    private String login;

    @UserPassword
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String firstName,
                           String lastName,
                           String patronymic,
                           String login,
                           String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
