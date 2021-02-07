package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.ServerDao;
import net.thumbtack.school.notes.dto.response.user.ServerSettingsResponse;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.*;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * DataAccessObject to debug application (to use in tests)
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class ServerDaoImpl implements ServerDao {
    final UserMapper userMapper;
    final NoteMapper noteMapper;
    final SectionMapper sectionMapper;
    final CommentMapper commentMapper;
    final SessionMapper sessionMapper;

    /**
     * Server settings, max user name length
     * Set in application properties
     */
    @Value("${max_name_length}")
    String maxNameLength;

    /**
     * Server settings, min user password length
     * Set in application properties
     */
    @Value("${min_password_length}")
    String minPasswordLength;

    /**
     * Server settings, time for which user session is alive
     * Set in application properties
     */
    @Value("${user_idle_timeout}")
    String userIdleTimeout;

    /**
     * Method to delete all sections, messages, notes and users from the server
     */
    @Override
    public void clear() {
        log.info("Trying to clear database");
        try {
            commentMapper.deleteAll();
            noteMapper.deleteAll();
            sectionMapper.deleteAll();
            sessionMapper.deleteAll();
            userMapper.deleteAll();
            log.info("Database was cleared");
        } catch (RuntimeException ex) {
            log.error("Can't clear database");
            throw ex;
        }
    }

    /**
     * Method to save information about user in database
     *
     * @return user in success
     */
    @Override
    public User registerUser() throws NoteServerException {
        log.info("Trying to save user account to database");
        try {
            User user = new User();
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setLogin("userLogin" + (int) (Math.random() * 10000 + 1));
            user.setPassword("user_password");
            userMapper.registerUser(user);
            return userMapper.getUserById(user.getId()).orElseThrow(() ->
                    new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, "exception"));
        } catch (RuntimeException ex) {
            log.error("Can't save user to database");
            throw ex;
        }
    }

    /**
     * Method to save information about user session in database
     *
     * @param userId identifier of user
     * @return session token of user session
     */
    @Override
    public String logInUser(int userId) {
        log.info("Trying to save user session to database");
        try {
            Session session = new Session();
            String sessionId = UUID.randomUUID().toString();
            session.setSessionId(sessionId);
            session.setExpiryTime(60);
            sessionMapper.startUserSession(session, userId);
            return sessionId;
        } catch (RuntimeException ex) {
            log.error("Can't save user session to database");
            throw ex;
        }
    }

    /**
     * Method that makes user an administrator
     *
     * @param user user account information who will be administrator
     * @return admin user account information
     */
    @Override
    public User makeAdmin(User user) {
        log.info("Trying to make admin");
        try {
            User resultUser = userMapper.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
            resultUser.setUserStatus(UserStatus.ADMIN);
            userMapper.changeUserStatus(resultUser);
            return resultUser;
        } catch (RuntimeException ex) {
            log.error("Can't create admin user");
            throw ex;
        }
    }

    /**
     * Method to get information about user with given login
     *
     * @param login login of the user to get information about
     * @return user account information
     * @throws NoteServerException if there is no user account on the server with such login
     */
    @Override
    public User getUserByLogin(String login) throws NoteServerException {
        log.info("Trying to get user");
        try {
            User user = userMapper.getUserByLogin(login);
            log.info("User with login {} was got", login);
            return user;
        } catch (NullPointerException ex) {
            log.error("No user with login = {} in database", login);
            throw new NoteServerException(ExceptionErrorInfo.USER_DOES_NOT_EXISTS, login);
        } catch (RuntimeException ex) {
            log.error("Can't get user");
            throw ex;
        }
    }

    /**
     * Method to get current server settings
     *
     * @return response, which contains current server settings
     */
    @Override
    public ServerSettingsResponse getServerSettings() {
        return new ServerSettingsResponse(
                maxNameLength,
                minPasswordLength,
                userIdleTimeout);
    }
}
