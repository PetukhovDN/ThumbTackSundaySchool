package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class describing user account model
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * User identifier
     */
    int id;

    /**
     * User first name
     */
    String firstName;

    /**
     * User last name
     */
    String lastName;

    /**
     * User patronymic
     */
    String patronymic;

    /**
     * User login
     */
    String login;

    /**
     * User password
     */
    String password;

    /**
     * User account time of creation
     */
    LocalDateTime creationTime;

    /**
     * If true - deleted from server
     * Default false
     */
    boolean deleted;

    /**
     * If true - is online on the server
     */
    boolean online;

    /**
     * User status
     */
    UserStatus userStatus;

    /**
     * Ratings for all notes of which current user is the author
     */
    List<Integer> ratings;

    /**
     * Average rating of the user
     */
    double rating;

    /**
     * List of all users that current user follow
     */
    List<User> following;

    /**
     * List of all users which follow current user
     */
    List<User> followers;

    /**
     * List of all users that current user ignores
     */
    List<User> ignoring;

    /**
     * List of all users which ignore current user
     */
    List<User> ignoredBy;

    /**
     * All notes of which current user is the author
     */
    List<Note> userNotes;

    /**
     * All sections of which current user is the author
     */
    List<Section> userSections;
}
