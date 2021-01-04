package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.ServerDao;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.*;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * DataAccessObject to debug application (to use in tests)
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class ServerDaoImpl implements ServerDao {
    UserMapper userMapper;
    NoteMapper noteMapper;
    SectionMapper sectionMapper;
    CommentMapper commentMapper;
    SessionMapper sessionMapper;

    /**
     * Method to delete all sections, messages, notes and users from the server
     */
    @Override
    public void clear() {
        log.info("Trying to clear database");
        try {
            userMapper.deleteAll();
            noteMapper.deleteAll();
            sectionMapper.deleteAll();
            commentMapper.deleteAll();
            sessionMapper.deleteAll();
            log.info("Database was cleared");
        } catch (RuntimeException ex) {
            log.error("Can't clear database");
            throw ex;
        }
    }

    /**
     * Method that makes user an administrator
     * Also user will be registered and logged in to the server
     *
     * @param user user to become administrator
     * @return session id of given user in success
     */
    @Override
    public String makeAdmin(User user) {
        log.info("Trying to make admin");
        try {
            Session session = new Session();
            session.setSessionId(UUID.randomUUID().toString());
            userMapper.registerUser(user);
            User resultUser = userMapper.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
            resultUser.setUserStatus(UserStatus.ADMIN);
            userMapper.changeUserStatus(resultUser);
            sessionMapper.startUserSession(session, resultUser.getId());
            return session.getSessionId();
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
}
