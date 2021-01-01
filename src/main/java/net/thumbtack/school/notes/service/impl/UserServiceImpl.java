package net.thumbtack.school.notes.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserServiceImpl implements UserService {
    final UserDao userDao;
    final SessionDao sessionDao;
    final String JAVASESSIONID = "JAVASESSIONID";

    @Value("${user_idle_timeout}")
    int user_idle_timeout;

    public UserServiceImpl(UserDao userDao, SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }

    @Override
    @Transactional
    public UserInfoResponse registerUser(RegisterRequest userRequest, HttpServletResponse response) throws NoteServerException {

        log.info("Trying to register user");
        User user = UserMapStruct.INSTANCE.requestRegisterUser(userRequest);
        User registeredUser = userDao.registerUser(user);
        String sessionToken = UUID.randomUUID().toString();
        Session userSession = new Session();
        userSession.setSessionId(sessionToken);
        userSession.setExpiryTime(user_idle_timeout);
        sessionDao.logInUser(registeredUser.getLogin(), registeredUser.getPassword(), userSession);

        Cookie cookie = new Cookie(JAVASESSIONID, sessionToken);
        response.addCookie(cookie);
        return UserMapStruct.INSTANCE.responseRegisterUser(registeredUser);
    }

    @Override
    @Transactional
    public String loginUser(LoginRequest loginRequest, HttpServletResponse response) throws NoteServerException {

        log.info("Trying to login user");
        String sessionToken = UUID.randomUUID().toString();
        Session userSession = new Session();
        userSession.setSessionId(sessionToken);
        userSession.setExpiryTime(user_idle_timeout);
        String resultSessionToken = sessionDao.logInUser(loginRequest.getLogin(), loginRequest.getPassword(), userSession);

        Cookie cookie = new Cookie(JAVASESSIONID, sessionToken);
        response.addCookie(cookie);
        return resultSessionToken;
    }

    @Override
    @Transactional
    public void logoutUser(String sessionToken) throws NoteServerException {

        log.info("Trying to logout user");
        sessionDao.stopUserSession(sessionToken);
    }

    @Override
    @Transactional
    public UserInfoResponse getUserInfo(String sessionToken) throws NoteServerException {

        log.info("Trying to get user info");
        if (sessionToken == null) {
            log.error("User is not logged in");
            throw new NoteServerException(ExceptionErrorInfo.USER_IS_NOT_LOGGED_IN, "Please log in");
        }
        Session userSession = checkSessionExpired(sessionToken);
        User user = userDao.getUserInfo(userSession.getUserId());
        return UserMapStruct.INSTANCE.responseRegisterUser(user);
    }

    @Override
    @Transactional
    public void leaveServer(LeaveServerRequest leaveRequest, String sessionToken) throws NoteServerException {

        log.info("Trying to delete user account");

        int userId = sessionDao.getSessionById(sessionToken).getUserId();
        sessionDao.stopUserSession(sessionToken);
        userDao.leaveNotesServer(userId, leaveRequest.getPassword());
    }


    public Session checkSessionExpired(String sessionToken) throws NoteServerException {
        Session userSession = sessionDao.getSessionById(sessionToken);
        long sessionStartTimeInSec = userSession
                .getLastAccessTime()
                .atZone(ZoneId.of("Asia/Omsk"))
                .toInstant()
                .toEpochMilli() / 1000;
        long currentTimeInSec = LocalDateTime.now().atZone(ZoneId.of("Asia/Omsk")).toInstant().toEpochMilli() / 1000;
        if (currentTimeInSec > sessionStartTimeInSec + user_idle_timeout) {
            log.info(String.valueOf(sessionStartTimeInSec));
            log.info(String.valueOf(user_idle_timeout));
            log.info(String.valueOf(currentTimeInSec));
            log.error("Session expired");
            sessionDao.stopUserSession(sessionToken);
            throw new NoteServerException(ExceptionErrorInfo.SESSION_EXPIRED, "Please log in");
        }
        return userSession;
    }
}
