package net.thumbtack.school.notes.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.mappers.SessionMapper;
import net.thumbtack.school.notes.mappers.UserMapper;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.params.UserRequestParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDaoImpl implements UserDao {
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    @Override
    public User registerUser(User user) {
        log.info("DAO insert User {} to Database", user);
        try {
            userMapper.registerUser(user);
            UUID userToken = UUID.randomUUID();
            user = userMapper.getUserByLogin(user.getLogin(), user.getPassword());
            sessionMapper.loginToDatabase(userToken.toString(), user.getId());
            user.setOnline(true);
        } catch (RuntimeException ex) {
            log.error("Can't insert User {} to Database, {}", user, ex);
            throw ex;
        }
        return user;
    }

    @Override
    public User getUserInfo(String userToken) {
        return null;
    }

    @Override
    public void leaveNotesServer(String userToken, String password) {

    }

    @Override
    public User editUserInfo(String userToken, String newFirstName, String newLastName, String newPatronymic, String password, String nePassword) {
        return null;
    }

    @Override
    public void giveAdminRoot(String userToken, int userId) {

    }

    @Override
    public List<User> getUsersWithParams(String userToken, UserRequestParam param) {
        return null;
    }

    @Override
    public void followUser(String userToken, String login) {

    }

    @Override
    public void ignoreUser(String userToken, String login) {

    }

    @Override
    public void stopFollowUser(String userToken, String login) {

    }

    @Override
    public void stopIgnoreUser(String userToken, String login) {

    }
}
