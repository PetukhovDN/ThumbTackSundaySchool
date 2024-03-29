package net.thumbtack.school.notes.dao;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionDaoTest {
    Session testUserSession;
    int registeredUserId;

    @Autowired
    ServerDao serverDao;

    @Autowired
    UserDao userDao;

    @Autowired
    SessionDao sessionDao;

    @BeforeEach
    public void setUp() throws NoteServerException {
        serverDao.clear();
        RegisterRequest rightRegisterRequest = new RegisterRequest(
                "Test",
                "Testov",
                "Testovitch",
                "login",
                "good_password");
        registeredUserId = userDao.registerUser(UserMapStruct.INSTANCE.requestRegisterUser(rightRegisterRequest));
        testUserSession = new Session();
        testUserSession.setSessionId(UUID.randomUUID().toString());
        testUserSession.setExpiryTime(60);
        testUserSession.setCreationTime(LocalDateTime.now());
    }

    @Test
    public void testLoginUser_rightParameters() throws NoteServerException {
        String sessionId = sessionDao.logInUser(registeredUserId, testUserSession)
                .getSessionId();
        assertEquals(testUserSession.getSessionId(), sessionId);
    }

    @Test
    public void testGetUserIdBySessionId_rightParameters() throws NoteServerException {
        String sessionId = sessionDao.logInUser(registeredUserId, testUserSession).getSessionId();
        int userId = sessionDao.getSessionBySessionId(sessionId).getUserId();

        assertEquals(registeredUserId, userId);
    }

    @Test
    public void testGetUserIdBySessionId_wrongSessionId() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sessionDao.getSessionBySessionId("wrong_session_id");
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such session on the server"))
        );
    }

    @Test
    public void testUpdateSession_rightSessionId() throws NoteServerException {
        sessionDao.logInUser(registeredUserId, testUserSession);
        Session session = sessionDao.getSessionBySessionId(testUserSession.getSessionId());
        LocalDateTime creationTime = session.getCreationTime();
        LocalDateTime lastAccessTime = LocalDateTime.now().plusNanos(1);
        session.setLastAccessTime(lastAccessTime);

        sessionDao.updateSession(session);
        Session resultSession = sessionDao.getSessionBySessionId(testUserSession.getSessionId());
        assertAll(
                () -> assertEquals(creationTime, resultSession.getCreationTime()),
                () -> assertEquals(session.getSessionId(), resultSession.getSessionId())
        );
    }
}
