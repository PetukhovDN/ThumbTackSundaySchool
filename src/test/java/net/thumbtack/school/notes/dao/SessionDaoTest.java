package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class SessionDaoTest {
    private User rightParametersUser;
    private String sessionToken;
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

        sessionToken = UUID.randomUUID().toString();
    }

    @Test
    public void testLoginUser_rightParameters() throws NoteServerException {
        userDao.registerUser(rightParametersUser);
        String sessionId = sessionDao.logInUser(rightParametersUser.getLogin(), rightParametersUser.getPassword(), sessionToken);
        assertEquals(sessionToken, sessionId);
    }

    @Test
    public void testLoginUser_loginDoesntExists() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sessionDao.logInUser(
                    rightParametersUser.getLogin(),
                    rightParametersUser.getPassword(),
                    sessionToken);
        });
        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("User with this login not registered on server"))
        );

    }

    @Test
    public void testGetUserIdBySessionId_rightParameters() throws NoteServerException {
        int registeredUserId = userDao.registerUser(rightParametersUser).getId();
        String sessionId = sessionDao.logInUser(
                rightParametersUser.getLogin(),
                rightParametersUser.getPassword(),
                sessionToken);
        int userId = sessionDao.getUserIdBySessionId(sessionId);

        assertEquals(registeredUserId, userId);
    }

    @Test
    public void testGetUserIdBySessionId_wrongParameters() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sessionDao.getUserIdBySessionId("wrong_session_id");
        });
        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such session on the server"))
        );
    }
}
