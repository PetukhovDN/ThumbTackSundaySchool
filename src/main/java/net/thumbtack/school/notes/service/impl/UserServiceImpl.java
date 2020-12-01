package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.SessionDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.dto.mappers.SessionMapStruct;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.RegisterResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDaoImpl userDao;
    // REVU private final SessionDao sessionDao;
    private final SessionDaoImpl sessionDao;

    // REVU private
    Base64.Encoder enc = Base64.getEncoder();

    @Override
    public RegisterResponse registerUser(RegisterRequest userRequest, HttpSession userSession) throws NoteServerException {

    	// REVU несколько действий, нужна трансакция
    	// см @Transactional
    	// обсуждение здесь
    	// https://stackoverflow.com/questions/41009873/how-to-use-transactional-annotation-in-mybatis-spring
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
        RegisterResponse registrationResponse = UserMapStruct.INSTANCE.responseRegisterUser(registeredUser);
        log.info("Response was got: {}", registrationResponse.toString());
        return registrationResponse;
    }

    @Override
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
    public void logoutUser(HttpSession userSession) throws NoteServerException {
        log.info("Trying to convert httpsession");
        log.info("SessionId: " + userSession.getId());
        Session currentSession = SessionMapStruct.INSTANCE.httpSessionToNotesSession(userSession);
        log.info("Session was converted");

        log.info("Trying to logout user");
        sessionDao.logOutUser(currentSession);
        log.info("The user logged out");
    }

}
