package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface SessionMapper {

    @Insert("INSERT INTO notes.session (token, note_user_id) VALUES ( #{userToken}, #{userId} )")
    @Options(keyProperty = "userToken")
    Integer loginToDatabase(String userToken, int userId);

    void logoutFromDatabase(String userToken);

    String checkIsLogged(String userToken);

}
