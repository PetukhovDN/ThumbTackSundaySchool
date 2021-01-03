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
            return userMapper.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
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
    public void leaveNotesServer(int userId) throws NoteServerException {
        log.info("DAO delete user account from database");
        try {
            User userById = userMapper.getUserById(userId);
            if (userById == null) {
                log.error("No user with userId = {} in database", userId);
                throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, String.valueOf(userId));
            }
            userMapper.deleteUser(userId);
        } catch (RuntimeException ex) {
            log.error("Can't get user info from Database, ", ex);
            throw ex;
        }
    }

    @Override
    public User editUserInfo(User user) {
        log.info("DAO update user account");
        try {
            userMapper.editUserInfo(user);
            return userMapper.getUserById(user.getId());
        } catch (RuntimeException ex) {
            log.error("Can't get user info from Database, ", ex);
            throw ex;
        }
    }

    @Override
    public void changeUserStatus(User user) {
        log.info("DAO give user admin root");
        try {
            userMapper.changeUserStatus(user);
        } catch (RuntimeException ex) {
            log.error("Can't give admin root to user with id {}, ", user.getId(), ex);
            throw ex;
        }
    }

    @Override
    public List<User> getAllUsers() {
        log.info("DAO get all users");
        try {
            return userMapper.getAllUsers();
        } catch (RuntimeException ex) {
            log.error("Can't get all users", ex);
            throw ex;
        }
    }

    @Override
    public void followUser(int currentUserId, int userIdToFollow) {
        log.info("DAO adding user with id {} to following list", userIdToFollow);
        try {
            userMapper.followUser(currentUserId, userIdToFollow);
        } catch (RuntimeException ex) {
            log.error("Can't add user with id {} to following list", userIdToFollow, ex);
            throw ex;
        }
    }

    @Override
    public void ignoreUser(int currentUserId, int userIdToFollow) {
        log.info("DAO adding user with id {} to ignoring list", userIdToFollow);
        try {
            userMapper.ignoreUser(currentUserId, userIdToFollow);
        } catch (RuntimeException ex) {
            log.error("Can't add user with id {} to ignoring list", userIdToFollow, ex);
            throw ex;
        }
    }

    @Override
    public void stopFollowUser(int currentUserId, int userIdToFollow) {
        log.info("DAO removing user with id {} from following list", userIdToFollow);
        try {
            userMapper.stopFollowing(currentUserId, userIdToFollow);
        } catch (RuntimeException ex) {
            log.error("Can't remove user with id {} from following list", userIdToFollow, ex);
            throw ex;
        }
    }

    @Override
    public void stopIgnoreUser(int currentUserId, int userIdToFollow) {
        log.info("DAO removing user with id {} from ignoring list", userIdToFollow);
        try {
            userMapper.stopIgnoring(currentUserId, userIdToFollow);
        } catch (RuntimeException ex) {
            log.error("Can't remove user with id {} from ignoring list", userIdToFollow, ex);
            throw ex;
        }
    }


    @Override
    public User getUserByLogin(String login) throws NoteServerException {
        log.info("Trying to get user");
        try {
            User user = userMapper.getUserByLogin(login);
            if (user == null) {
                log.error("No user with login = {} in database", login);
                throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, login);
            }
            return user;
        } catch (RuntimeException ex) {
            log.error("Can't get user");
            throw ex;
        }
    }
}
