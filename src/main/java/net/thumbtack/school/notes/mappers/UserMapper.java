package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    @Insert("INSERT INTO notes.note_user (first_name, last_name, patronymic, login, password, token)" +
            " VALUES " +
            "#{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password}, #{user.token} ")
    @Options(useGeneratedKeys = true, keyProperty = "note_user.id")
    Integer registerUser(@Param("user") User user);


    User getUserByLogin(String login, String password);


    @Delete("DELETE FROM notes.note_user")
    void deleteAll();
}
