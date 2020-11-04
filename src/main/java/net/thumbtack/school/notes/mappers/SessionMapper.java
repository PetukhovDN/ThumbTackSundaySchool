package net.thumbtack.school.notes.mappers;

public interface SessionMapper {

    Integer loginToDatabase(String userToken, int userId);

    void logoutFromDatabase(String userToken);

    String checkIsLogged(String userToken);

}
