package net.thumbtack.school.notes.dto.response.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.enums.UserStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UsersInfoResponse {
    int id;
    String firstName;
    String lastName;
    String patronymic;
    String login;
    LocalDateTime creationTime;
    boolean isOnline;
    boolean isDeleted;
    UserStatus userStatus;
    int rating;
}
