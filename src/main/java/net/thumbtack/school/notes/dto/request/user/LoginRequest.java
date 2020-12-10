package net.thumbtack.school.notes.dto.request.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.validator.UserLogin;
import net.thumbtack.school.notes.validator.UserPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @UserLogin
    String login;

    @UserPassword
    String password;
}
