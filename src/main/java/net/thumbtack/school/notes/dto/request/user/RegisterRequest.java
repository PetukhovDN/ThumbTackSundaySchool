package net.thumbtack.school.notes.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.UserLogin;
import net.thumbtack.school.notes.validator.UserName;
import net.thumbtack.school.notes.validator.UserPassword;
import net.thumbtack.school.notes.validator.UserPatronymic;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
