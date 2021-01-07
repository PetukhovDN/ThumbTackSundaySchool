package net.thumbtack.school.notes.dao;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserDaoTest {
    User rightParametersUser;
    RegisterRequest rightRegisterRequest;

    @Autowired
    ServerDao serverDao;

    @Autowired
    UserDao userDao;

    @BeforeEach
    public void setUp() {
        serverDao.clear();
        rightRegisterRequest = new RegisterRequest(
                "Test",
                "Testov",
                "Testovitch",
                "login",
                "test_password");
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

    @Test
    public void testGetUserInfo_rightParameters() throws NoteServerException {
        User registeredUser = userDao.registerUser(rightParametersUser);
        User userInfo = userDao.getUserById(registeredUser.getId());

        assertEquals(registeredUser.getFirstName(), userInfo.getFirstName());
    }

    @Test
    public void testGetUserInfo_userDoesNotExists() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            userDao.getUserById(1);

        });
        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such user on the server"))
        );
    }

    @Test
    public void testLeaveServer_loggedInUser() throws NoteServerException {
        User registeredUser = userDao.registerUser(rightParametersUser);
        userDao.changeUserDeletedStatusToDeleted(registeredUser.getId());

    }

    @Test
    public void testLeaveServer_userDoesNotExists() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            userDao.changeUserDeletedStatusToDeleted(100);

        });
        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such user on the server"))
        );
    }

    @Test
    public void testUpdateUserInfo_rightParameters() throws NoteServerException {
        User registeredUser = userDao.registerUser(rightParametersUser);
        registeredUser.setFirstName("NewFirstName");
        registeredUser.setLastName("NewLastName");
        userDao.editUserInfo(registeredUser);

        User updatedUser = userDao.getUserById(registeredUser.getId());

        assertAll(
                () -> assertEquals(registeredUser.getFirstName(), updatedUser.getFirstName()),
                () -> assertEquals(registeredUser.getLastName(), updatedUser.getLastName()),
                () -> assertEquals(registeredUser.getLogin(), updatedUser.getLogin())
        );
    }

    @Test
    public void testFollowUser_rightParameters() throws NoteServerException {
        User firstRegisteredUser = userDao.registerUser(rightParametersUser);
        rightRegisterRequest.setLogin("second_user_login");
        rightRegisterRequest.setLastName("SecondUserLastName");
        rightParametersUser = UserMapStruct.INSTANCE.requestRegisterUser(rightRegisterRequest);
        User secondRegisteredUser = userDao.registerUser(rightParametersUser);

        userDao.followUser(firstRegisteredUser.getId(), secondRegisteredUser.getId());
        List<User> followers = userDao.getUsersFollowedBy(firstRegisteredUser.getId());
        User follower = followers.get(0);

        assertAll(
                () -> assertEquals(secondRegisteredUser.getId(), follower.getId()),
                () -> assertEquals(secondRegisteredUser.getLastName(), follower.getLastName())
        );

        userDao.stopFollowUser(firstRegisteredUser.getId(), secondRegisteredUser.getId());
        List<User> followersEmptyList = userDao.getUsersFollowedBy(firstRegisteredUser.getId());

        assertAll(
                () -> assertEquals(0, followersEmptyList.size())
        );
    }

    @Test
    public void testIgnoreUser_rightParameters() throws NoteServerException {
        User firstRegisteredUser = userDao.registerUser(rightParametersUser);
        rightRegisterRequest.setLogin("second_user_login");
        rightRegisterRequest.setLastName("SecondUserLastName");
        rightParametersUser = UserMapStruct.INSTANCE.requestRegisterUser(rightRegisterRequest);
        User secondRegisteredUser = userDao.registerUser(rightParametersUser);

        userDao.ignoreUser(firstRegisteredUser.getId(), secondRegisteredUser.getId());
        List<User> ignoringUsers = userDao.getUsersIgnoredBy(firstRegisteredUser.getId());
        User ignoringUser = ignoringUsers.get(0);

        assertAll(
                () -> assertEquals(secondRegisteredUser.getId(), ignoringUser.getId()),
                () -> assertEquals(secondRegisteredUser.getLastName(), ignoringUser.getLastName())
        );

        userDao.stopIgnoreUser(firstRegisteredUser.getId(), secondRegisteredUser.getId());
        List<User> ignoringUsersEmptyList = userDao.getUsersIgnoredBy(firstRegisteredUser.getId());

        assertAll(
                () -> assertEquals(0, ignoringUsersEmptyList.size())
        );
    }
}