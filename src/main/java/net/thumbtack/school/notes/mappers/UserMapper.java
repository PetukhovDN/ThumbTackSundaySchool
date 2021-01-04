package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO note_user (first_name, last_name, patronymic, login, password)" +
            " VALUES " +
            "( #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password} )")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer registerUser(@Param("user") User user);

    @Select("SELECT id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user WHERE login = #{login} AND password = #{password}")
    User getUserByLoginAndPassword(String login, String password);

    @Select("SELECT id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user WHERE login = #{login}")
    User getUserByLogin(String login);

    @Select("SELECT id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user WHERE id = #{id}")
    User getUserById(int id);

    @Update("UPDATE note_user SET first_name = #{user.firstName}, last_name = #{user.lastName}, " +
            "patronymic = #{user.patronymic}, password = #{user.password}  WHERE id = #{user.id} ")
    void editUserInfo(@Param("user") User user);

    @Update("UPDATE note_user SET user_status = #{user.userStatus} WHERE id = #{user.id}")
    void changeUserStatus(@Param("user") User user);

    @Select("SELECT follower_user_id as id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user n LEFT JOIN following_user f ON n.id = f.follower_user_id WHERE following_user_id = #{id}")
    List<User> getFollowingTo(int id);

    @Select("SELECT following_user_id as id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user n LEFT JOIN following_user f ON n.id = f.following_user_id WHERE follower_user_id = #{id}")
    List<User> getFollowedBy(int id);

    @Select("SELECT ignored_by_user_id as id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user n LEFT JOIN ignore_user i ON n.id = i.ignored_by_user_id WHERE ignoring_user_id = #{id}")
    List<User> getIgnoringTo(int id);

    @Select("SELECT ignoring_user_id as id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted " +
            "FROM note_user n LEFT JOIN ignore_user i ON n.id = i.ignoring_user_id WHERE ignored_by_user_id = #{id}")
    List<User> getIgnoredBy(int id);

    @Select("SELECT rating FROM note_rating WHERE author_id = #{id}")
    List<Integer> getRatings(int id);

    @Select("SELECT id, first_name as firstName, last_name as lastName, patronymic, login, password, " +
            "user_creation_time as creationTime, user_status as userStatus, deleted_status as isDeleted FROM note_user")
    @Results({
            @Result(property = "following", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes.mappers.UserMapper.getFollowingTo", fetchType = FetchType.LAZY)),
            @Result(property = "followers", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes.mappers.UserMapper.getFollowedBy", fetchType = FetchType.LAZY)),
            @Result(property = "ignoring", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes.mappers.UserMapper.getIgnoringTo", fetchType = FetchType.LAZY)),
            @Result(property = "ignoredBy", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes.mappers.UserMapper.getIgnoredBy", fetchType = FetchType.LAZY)),
            @Result(property = "ratings", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes.mappers.UserMapper.getRatings", fetchType = FetchType.LAZY))})
    List<User> getAllUsers();

    @Insert("INSERT INTO following_user (follower_user_id, following_user_id)" +
            " VALUES " +
            "( #{currentUserId}, #{userIdToFollow} )")
    @Options(useGeneratedKeys = true)
    Integer followUser(int currentUserId, int userIdToFollow);

    @Delete("DELETE FROM following_user WHERE follower_user_id = #{currentUserId} AND following_user_id = #{followingUserId}")
    void stopFollowing(int currentUserId, int followingUserId);

    @Insert("INSERT INTO ignore_user (ignored_by_user_id, ignoring_user_id)" +
            " VALUES " +
            "( #{currentUserId}, #{userIdToIgnore} )")
    @Options(useGeneratedKeys = true)
    Integer ignoreUser(int currentUserId, int userIdToIgnore);

    @Delete("DELETE FROM ignore_user WHERE ignored_by_user_id = #{currentUserId} AND ignoring_user_id = #{ignoringUserId}")
    void stopIgnoring(int currentUserId, int ignoringUserId);

    @Update("UPDATE note_user SET deleted_status = #{user.isDeleted} WHERE id = #{user.id}")
    void leaveNotesServer(@Param("user") User user);

    @Delete("DELETE FROM note_user")
    void deleteAll();
}
