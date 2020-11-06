package net.thumbtack.school.notes.model;

import java.util.Objects;
import java.util.UUID;

public class Session {

    private User user;
    private UUID userToken;

    public Session() {
    }

    public Session(User user, UUID userToken) {
        this.user = user;
        this.userToken = userToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getUserToken() {
        return userToken;
    }

    public void setUserToken(UUID userToken) {
        this.userToken = userToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        Session session = (Session) o;
        return Objects.equals(getUserToken(), session.getUserToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserToken());
    }
}
