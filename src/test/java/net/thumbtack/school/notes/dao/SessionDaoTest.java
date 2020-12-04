package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.dao.impl.ServerDaoImpl;
import net.thumbtack.school.notes.dao.impl.SessionDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
public class SessionDaoTest {
    private User rightParametersUser;
    private Session testSession;
    private RegisterRequest rightRegisterRequest;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionDao sessionDao;

    @BeforeEach
    public void setUp() {
        serverDao.clear();
        rightRegisterRequest = new RegisterRequest(
                "Test",
                "Testov",
                "Testovitch",
                "login",
                "good_password");
        rightParametersUser = UserMapStruct.INSTANCE.requestRegisterUser(rightRegisterRequest);

        testSession = new Session();
        testSession.setSessionId("test_session_id");
    }

    @Test
    public void testLoginUser_rightParameters() throws NoteServerException {
        userDao.registerUser(rightParametersUser);
        String sessionId = sessionDao.logInUser(rightParametersUser.getLogin(), rightParametersUser.getPassword(),testSession);
        assertEquals(testSession.getSessionId(), sessionId);
    }

    @Test
    public void testLoginUser_loginDoesntExists() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sessionDao.logInUser(rightParametersUser.getLogin(), rightParametersUser.getPassword(),testSession);
        });
        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("User with this login not registered on server"))
        );

    }
}