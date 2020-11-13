package net.thumbtack.school.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private LocalDateTime creationTime;
    private UserStatus userStatus;
    private boolean isDeleted;
    private boolean isOnline;
    private int rating;

    private List<User> following;
    private List<User> followers;
    private List<User> ignoring;
    private List<User> ignoredBy;
}
