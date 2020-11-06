package net.thumbtack.school.notes.model;

import net.thumbtack.school.notes.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public User() {
    }

    public User(int id,
                String firstName,
                String lastName,
                String patronymic,
                String login,
                String password,
                LocalDateTime creationTime,
                UserStatus userStatus,
                boolean isDeleted,
                boolean isOnline,
                int rating) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.creationTime = creationTime;
        this.userStatus = userStatus;
        this.isDeleted = isDeleted;
        this.isOnline = isOnline;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getIgnoring() {
        return ignoring;
    }

    public void setIgnoring(List<User> ignoring) {
        this.ignoring = ignoring;
    }

    public List<User> getIgnoredBy() {
        return ignoredBy;
    }

    public void setIgnoredBy(List<User> ignoredBy) {
        this.ignoredBy = ignoredBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getPatronymic(), user.getPatronymic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPatronymic());
    }
}
