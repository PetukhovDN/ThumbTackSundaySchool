package net.thumbtack.school.notes.dto.request.user;

import net.thumbtack.school.notes.validator.UserLogin;
import net.thumbtack.school.notes.validator.UserName;
import net.thumbtack.school.notes.validator.UserPassword;

import javax.validation.constraints.NotNull;

public class RegisterRequest {

    @NotNull
    @UserName
    private String firstName;

    @NotNull
    @UserName
    private String lastName;

    @UserName
    private String patronymic;

    @NotNull
    @UserLogin
    private String login;

    @NotNull
    @UserPassword
    private String password;

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
