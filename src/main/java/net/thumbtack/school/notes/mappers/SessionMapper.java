package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Session;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO session (session_id, note_user_id) VALUES ( #{sessionId}, #{userId} )")
    @Options(keyProperty = "session.sessionId")
    Integer loginToDatabase(String sessionId, int userId);

    @Delete("DELETE FROM session WHERE session_id = #{sessionId}")
    void logoutFromDatabase(String sessionId);

    @Select("SELECT session_id FROM session WHERE session_id = #{sessionId}")
    String getUserSession(String sessionId);

    @Select("SELECT last_access_time FROM session WHERE session_id = #{sessionId}")
    Session checkSessionExpired(String sessionId);

    @Delete("DELETE FROM session")
    void deleteAll();
}
