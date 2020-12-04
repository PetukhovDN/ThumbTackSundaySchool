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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserDaoTest {
    private User rightParametersUser;
    private RegisterRequest rightRegisterRequest;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private UserDao userDao;

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
    }

    @Test
    public void testRegisterUser_rightParameters() throws NoteServerException {
        User registeredUser = userDao.registerUser(rightParametersUser);
        assertEquals(rightParametersUser.getFirstName(), registeredUser.getFirstName());
    }

    @Test
    public void testRegisterUser_wrongParameters() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            userDao.registerUser(rightParametersUser);
            User rightParametersUserSameLogin = UserMapStruct.INSTANCE.requestRegisterUser(rightRegisterRequest);
            userDao.registerUser(rightParametersUserSameLogin);
        });
        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("User with this login already exists"))
        );
    }


}