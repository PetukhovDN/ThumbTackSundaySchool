package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Session;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO session (session_id, note_user_id, expiry_time) VALUES ( #{session.sessionId}, #{userId}, #{session.expiryTime} )")
    @Options(useGeneratedKeys = true, keyProperty = "session.id")
    Integer startUserSession(@Param("session")Session session, int userId);

    @Delete("DELETE FROM session WHERE session_id = #{sessionId}")
    void stopUserSession(String sessionId);

    @Select("SELECT id, session_id as sessionId, note_user_id as userId, session_start_time as creationTime, " +
            "last_access_time as lastAccessTime, expiry_time as expiryTime " +
            "FROM session WHERE session_id = #{sessionId}")
    Session getSessionBySessionId(String sessionId);

    @Select("SELECT id, session_id as sessionId, note_user_id as userId, session_start_time as creationTime, " +
            "last_access_time as lastAccessTime, expiry_time as expiryTime " +
            "FROM session WHERE note_user_id = #{userId}")
    Session getSessionByUserId(int userId);

    @Delete("DELETE FROM session")
    void deleteAll();
}
