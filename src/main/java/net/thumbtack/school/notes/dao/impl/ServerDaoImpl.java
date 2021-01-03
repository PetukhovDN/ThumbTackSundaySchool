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


    @Override
    public void clear() {
        log.info("Trying to clear database");
        try {
            userMapper.deleteAll();
            noteMapper.deleteAll();
            sectionMapper.deleteAll();
            commentMapper.deleteAll();
            sessionMapper.deleteAll();
        } catch (RuntimeException ex) {
            log.error("Can't clear database");
            throw ex;
        }
        log.info("Database was cleared");
    }

    @Override
    public String makeAdmin(User user) {
        log.info("Trying to make admin");
        Session session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        try {
            userMapper.registerUser(user);
            User resultUser = userMapper.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
            resultUser.setUserStatus(UserStatus.ADMIN);
            userMapper.changeUserStatus(resultUser);
            sessionMapper.startUserSession(session, resultUser.getId());
        } catch (RuntimeException ex) {
            log.error("Can't create admin user");
            throw ex;
        }
        log.info("User with admin root was created");
        return session.getSessionId();
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
            log.info("User with login {} was got", login);
            return user;
        } catch (RuntimeException ex) {
            log.error("Can't get user");
            throw ex;
        }
    }
}
