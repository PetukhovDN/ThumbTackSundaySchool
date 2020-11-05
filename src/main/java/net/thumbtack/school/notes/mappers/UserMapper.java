package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {
    @Insert("INSERT INTO notes.note_user (first_name, last_name, patronymic, login, password)" +
            " VALUES " +
            "( #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password} )")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer registerUser(@Param("user") User user);

    @Select("SELECT id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user WHERE login = #{login} AND password = #{password}")
    User getUserByLogin(String login, String password);


    @Delete("DELETE FROM notes.note_user")
    void deleteAll();
}
