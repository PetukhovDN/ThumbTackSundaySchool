package net.thumbtack.school.notes.dto.response.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.enums.UserStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersInfoResponse {
    int id;
    String firstName;
    String lastName;
    String patronymic;
    String login;
    LocalDateTime creationTime;
    boolean online;
    boolean deleted;
    UserStatus userStatus;
    double rating;
}
