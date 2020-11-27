package net.thumbtack.school.notes.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.UserLogin;
import net.thumbtack.school.notes.validator.UserPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @UserLogin
    private String login;

    @UserPassword
    private String password;
}
