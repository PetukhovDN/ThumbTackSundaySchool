package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int id;
    String firstName;
    String lastName;
    String patronymic;
    String login;
    String password;
    LocalDateTime creationTime;
    boolean deleted;
    boolean online;
    UserStatus userStatus;
    List<Integer> ratings;

    List<User> following;
    List<User> followers;
    List<User> ignoring;
    List<User> ignoredBy;
    List<Note> userNotes;
    List<Section> userSections;
}
