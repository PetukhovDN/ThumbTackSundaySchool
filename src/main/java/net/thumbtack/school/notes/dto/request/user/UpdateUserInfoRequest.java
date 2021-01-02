package net.thumbtack.school.notes.dto.request.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.validator.UserName;
import net.thumbtack.school.notes.validator.UserPassword;
import net.thumbtack.school.notes.validator.UserPatronymic;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserInfoRequest {
    @UserName
    String firstName;

    @UserName
    String lastName;

    @UserPatronymic
    String patronymic;

    @UserPassword
    String oldPassword;

    @UserPassword
    String password;
}
