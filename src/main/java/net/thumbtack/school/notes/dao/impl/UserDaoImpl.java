package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.UserMapper;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.params.UserRequestParam;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class UserDaoImpl implements UserDao {
    UserMapper userMapper;

    @Override
    public User registerUser(User user) throws NoteServerException {
        log.info("DAO insert User {} to Database", user);
        try {
            userMapper.registerUser(user);
            return userMapper.getUserByLogin(user.getLogin(), user.getPassword());
        } catch (DuplicateKeyException ex) {
            log.error("Login {} already exists", user.getLogin(), ex);
            throw new NoteServerException(ExceptionErrorInfo.LOGIN_ALREADY_EXISTS, user.getLogin());
        } catch (RuntimeException ex) {
            log.error("Can't insert User {} to Database, {}", user, ex);
            throw ex;
        }
    }

    @Override
    public User getUserInfo(int userId) throws NoteServerException {
        log.info("DAO get user info from Database");
        try {
            User userById = userMapper.getUserById(userId);
            if (userById == null) {
                log.error("No user with userId = {} in database", userId);
                throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, String.valueOf(userId));
            }
            return userById;
        } catch (RuntimeException ex) {
            log.error("Can't get user info from Database, ", ex);
            throw ex;
        }
    }

    @Override
    public void leaveNotesServer(int userId, String password) throws NoteServerException {
        log.info("DAO delete user account from database");
        try {
            String userPassword = userMapper.getUserById(userId).getPassword();
            if (!password.equals(userPassword)) {
                log.error("Wrong password {}", password);
                throw new NoteServerException(ExceptionErrorInfo.WRONG_PASSWORD, password);
            }
            userMapper.deleteUser(userId);
        } catch (NullPointerException ex) {
            log.error("No such user on the server");
            throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, String.valueOf(userId));
        } catch (RuntimeException ex) {
            log.error("Can't get user info from Database, ", ex);
            throw ex;
        }
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
