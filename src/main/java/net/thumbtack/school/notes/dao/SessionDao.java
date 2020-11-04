package net.thumbtack.school.notes.dao;

public interface SessionDao {
    String logInUser(String login, String password);

    void logOutUser(String userToken);
}
