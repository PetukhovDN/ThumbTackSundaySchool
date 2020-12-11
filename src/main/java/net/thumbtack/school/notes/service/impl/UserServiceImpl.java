package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.dto.mappers.SessionMapStruct;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final SessionDao sessionDao;

    private Base64.Encoder enc = Base64.getEncoder();

    @Override
    @Transactional
    public UserInfoResponse registerUser(RegisterRequest userRequest, HttpSession userSession) throws NoteServerException {

    	// REVU слишком много вывода в лог тоже не очень хорошо
    	// тем более на уровне info. Это же пойдет в production
    	// думаю, на весь метод хватило бы одного в начале
    	// если он прологгируется, значит, мы тут были
    	// а если в итоге регистрация не прошла, то надо смотреть логгинг исключений
        log.info("Trying to register user");
        User user = UserMapStruct.INSTANCE.requestRegisterUser(userRequest);
        User registeredUser = userDao.registerUser(user);
        log.info("The user was registered");

        log.info("Trying to convert httpsession");
        Session currentSession = SessionMapStruct.INSTANCE.httpSessionToNotesSession(userSession);
        log.info("Session was converted");

        log.info("Trying to login user");
        sessionDao.logInUser(registeredUser.getLogin(), registeredUser.getPassword(), currentSession);
        log.info("The user logged in");

        log.info("Trying to get registration response");
        UserInfoResponse registrationResponse = UserMapStruct.INSTANCE.responseRegisterUser(registeredUser);
        log.info("Response was received: {}", registrationResponse.toString());
        return registrationResponse;
    }

    @Override
    @Transactional
    public String loginUser(LoginRequest loginRequest, HttpSession userSession) throws NoteServerException {
        log.info("Trying to convert httpsession");
        log.info("SessionId: " + userSession.getId());
        Session currentSession = SessionMapStruct.INSTANCE.httpSessionToNotesSession(userSession);
        log.info("Session was converted");

        log.info("Trying to login user");
        String resultOfLogin = sessionDao.logInUser(loginRequest.getLogin(), loginRequest.getPassword(), currentSession);
        log.info("The user logged in");
        return resultOfLogin;
    }

    @Override
    @Transactional
    public void logoutUser(HttpSession userSession) throws NoteServerException {
        log.info("Trying to convert httpsession");
        log.info("SessionId: " + userSession.getId());
        Session currentSession = SessionMapStruct.INSTANCE.httpSessionToNotesSession(userSession);
        log.info("Session was converted");

        log.info("Trying to logout user");
        sessionDao.logOutUser(currentSession);
        log.info("The user logged out");
    }

    @Override
    @Transactional
    public UserInfoResponse getUserInfo(HttpSession userSession) throws NoteServerException {
        if (userSession == null) {
            throw new NoteServerException(ExceptionErrorInfo.USER_IS_NOT_LOGGED_IN, "Please log in");
        }
        log.info("Trying to convert httpsession");
        log.info("SessionId: " + userSession.getId());
        Session currentSession = SessionMapStruct.INSTANCE.httpSessionToNotesSession(userSession);
        log.info("Session was converted");

        log.info("Trying to get user id");
        int userId = sessionDao.getUserIdBySessionId(currentSession.getSessionId());
        log.info("The user id was received");

        log.info("Trying to get user info");
        User user = userDao.getUserInfo(userId);
        log.info("The user info was received");

        log.info("Trying to get user info response");
        UserInfoResponse userInfo = UserMapStruct.INSTANCE.responseRegisterUser(user);
        log.info("Response was received: {}", userInfo.toString());
        return userInfo;
    }

    @Override
    @Transactional
    public void leaveServer(LeaveServerRequest leaveRequest, HttpSession userSession) throws NoteServerException {
        log.info("Trying to convert httpsession");
        log.info("SessionId: " + userSession.getId());
        Session currentSession = SessionMapStruct.INSTANCE.httpSessionToNotesSession(userSession);
        log.info("Session was converted");

        log.info("Trying to get user id");
        int userId = sessionDao.getUserIdBySessionId(currentSession.getSessionId());
        log.info("The user id was received");

        log.info("Trying to logout user");
        sessionDao.logOutUser(currentSession);
        log.info("The user logged out");

        log.info("Trying to delete user account");
        userDao.leaveNotesServer(userId, leaveRequest.getPassword());
        log.info("The user account was deleted");
    }


}
