package net.thumbtack.school.notes.model;

import java.util.Date;
import java.util.Objects;

public class User {

    private final String firstName;
    private final String lastName;
    private final String patronymic;
    private final String login;
    private final String password;
    private final Date creationTime;
    private final boolean isOnline;
    private int id;
    private boolean isAdmin;
    private boolean isDeleted;


    public User(String firstName,
                String lastName,
                String patronymic,
                String login,
                String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.creationTime = new Date();
        this.isOnline = true;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getLogin() {
        return login;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
