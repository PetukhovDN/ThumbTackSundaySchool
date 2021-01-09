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

/**
 * DataAccessObject to work with user accounts
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class UserDaoImpl implements UserDao {
    UserMapper userMapper;

    /**
     * Method to save user information to the database
     *
     * @param user user account information
     * @return user account identifier in success
     * @throws NoteServerException if user with the same login is already saved to the database
     */
    @Override
    public Integer registerUser(User user) throws NoteServerException {
        log.info("DAO insert User {} to Database", user);
        try {
            userMapper.registerUser(user);
            return user.getId();
        } catch (DuplicateKeyException ex) {
            log.error("Login {} already exists", user.getLogin(), ex);
            throw new NoteServerException(ExceptionErrorInfo.LOGIN_ALREADY_EXISTS, user.getLogin());
        } catch (RuntimeException ex) {
            log.error("Can't insert User {} to Database, {}", user, ex);
            throw ex;
        }
    }

    /**
     * Method to get user account information from database
     *
     * @param userId identifier of user account
     * @return user account information in success
     * @throws NoteServerException if there is no user account with such identifier, saved in database
     */
    @Override
    public User getUserById(int userId) throws NoteServerException {
        log.info("DAO get user info from Database");
        try {
            User userById = userMapper.getUserById(userId);
            log.info("User with login {} was got from database", userById.getLogin());
            return userById;
        } catch (NullPointerException ex) {
            log.error("No user with id = {} in database", userId);
            throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, String.valueOf(userId));
        } catch (RuntimeException ex) {
            log.error("Can't get user info from Database, ", ex);
            throw ex;
        }
    }

    /**
     * Method to change user deleted status to deleted in database
     *
     * @param user user account information
     * @throws NoteServerException if there is no user account with such identifier, saved in database
     */
    @Override
    public Integer changeUserDeletedStatusToDeleted(User user) throws NoteServerException {
        log.info("DAO delete user account from database");
        try {
            userMapper.leaveNotesServer(user);
            return user.getId();
        } catch (NullPointerException ex) {
            log.error("No user with id {} in database", user.getId());
            throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, "No such user on the server");
        } catch (RuntimeException ex) {
            log.error("Can't get user info from Database, ", ex);
            throw ex;
        }
    }

    /**
     * Method to change user account information in database
     *
     * @param user user account to be changed
     * @return user identifier in success
     */
    @Override
    public Integer editUserInfo(User user) {
        log.info("DAO update user account");
        try {
            userMapper.editUserInfo(user);
            return user.getId();
        } catch (RuntimeException ex) {
            log.error("Can't get user info from Database, ", ex);
            throw ex;
        }
    }

    /**
     * Method to change information about user status in database
     *
     * @param user user account to be changed
     * @return user identifier in success
     */
    @Override
    public Integer changeUserStatus(User user) {
        log.info("DAO give user admin root");
        try {
            userMapper.changeUserStatus(user);
            return user.getId();
        } catch (RuntimeException ex) {
            log.error("Can't give admin root to user with id {}, ", user.getId(), ex);
            throw ex;
        }
    }

    /**
     * Method to get list of all user accounts from database
     *
     * @return list of user accounts
     */
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

    /**
     * Method to change information in database about user followings (start following user)
     *
     * @param currentUserId  identifier of current user
     * @param userIdToFollow identifier of user to start following
     */
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

    /**
     * Method to change information in database about users, which current user ignore (start ignoring)
     *
     * @param currentUserId  identifier of current user
     * @param userIdToIgnore identifier of user to start ignoring
     */
    @Override
    public void ignoreUser(int currentUserId, int userIdToIgnore) {
        log.info("DAO adding user with id {} to ignoring list", userIdToIgnore);
        try {
            userMapper.ignoreUser(currentUserId, userIdToIgnore);
        } catch (RuntimeException ex) {
            log.error("Can't add user with id {} to ignoring list", userIdToIgnore, ex);
            throw ex;
        }
    }

    /**
     * Method to change information in database about user followings (stop following)
     *
     * @param currentUserId         identifier of current user
     * @param userIdToStopFollowing identifier of user to stop following
     */
    @Override
    public void stopFollowUser(int currentUserId, int userIdToStopFollowing) {
        log.info("DAO removing user with id {} from following list", userIdToStopFollowing);
        try {
            userMapper.stopFollowing(currentUserId, userIdToStopFollowing);
        } catch (RuntimeException ex) {
            log.error("Can't remove user with id {} from following list", userIdToStopFollowing, ex);
            throw ex;
        }
    }

    /**
     * Method to change information in database about users, which current user ignore (stop ignoring)
     *
     * @param currentUserId        identifier of current user
     * @param userIdToStopIgnoring identifier of user to stop ignoring
     */
    @Override
    public void stopIgnoreUser(int currentUserId, int userIdToStopIgnoring) {
        log.info("DAO removing user with id {} from ignoring list", userIdToStopIgnoring);
        try {
            userMapper.stopIgnoring(currentUserId, userIdToStopIgnoring);
        } catch (RuntimeException ex) {
            log.error("Can't remove user with id {} from ignoring list", userIdToStopIgnoring, ex);
            throw ex;
        }
    }


    /**
     * Method to get user account information from database
     *
     * @param login login of user
     * @return user account information in success
     * @throws NoteServerException if there is no user account with such login
     */
    @Override
    public User getUserByLogin(String login) throws NoteServerException {
        log.info("Trying to get user");
        try {
            User userByLogin = userMapper.getUserByLogin(login);
            log.info("User with login {} was got from database", userByLogin.getLogin());
            return userByLogin;
        } catch (NullPointerException ex) {
            log.error("No user with login = {} in database", login);
            throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, login);
        } catch (RuntimeException ex) {
            log.error("Can't get user");
            throw ex;
        }
    }

    /**
     * Method to get information about user accounts, which user with given identifier is following
     *
     * @param userId user identifier
     * @return list of user accounts information in success
     */
    @Override
    public List<User> getUsersFollowingTo(int userId) {
        log.info("Trying to get users following to");
        try {
            return userMapper.getFollowingTo(userId);
        } catch (RuntimeException ex) {
            log.error("Can't get users, which user with id {} is following", userId);
            throw ex;
        }
    }

    /**
     * Method to get information about user accounts, which are following user with given identifier
     *
     * @param userId user identifier
     * @return list of user accounts information in success
     */
    @Override
    public List<User> getUsersFollowedBy(int userId) {
        log.info("Trying to get users followed by");
        try {
            return userMapper.getFollowedBy(userId);
        } catch (RuntimeException ex) {
            log.error("Can't get users, which are following user with id {}", userId);
            throw ex;
        }
    }

    /**
     * Method to get information about user accounts, which user with given identifier is ignoring
     *
     * @param userId user identifier
     * @return list of user accounts information in success
     */
    @Override
    public List<User> getUsersIgnoringTo(int userId) {
        log.info("Trying to get users ignoring to");
        try {
            return userMapper.getIgnoringTo(userId);
        } catch (RuntimeException ex) {
            log.error("Can't get users, which user with id {} is ignoring", userId);
            throw ex;
        }
    }

    /**
     * Method to get information about user accounts, which are ignoring user with given identifier
     *
     * @param userId user identifier
     * @return list of user accounts information in success
     */
    @Override
    public List<User> getUsersIgnoredBy(int userId) {
        log.info("Trying to get users ignored by");
        try {
            return userMapper.getIgnoredBy(userId);
        } catch (RuntimeException ex) {
            log.error("Can't get users, which are ignoring user with id {}", userId);
            throw ex;
        }
    }
}
