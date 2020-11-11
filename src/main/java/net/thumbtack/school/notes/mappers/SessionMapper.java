package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO notes.session (token, note_user_id) VALUES ( #{userToken}, #{userId} )")
    @Options(keyProperty = "userToken")
    Integer loginToDatabase(String userToken, int userId);

    void logoutFromDatabase(int userId);

    String getUserSession(int userId);

}
