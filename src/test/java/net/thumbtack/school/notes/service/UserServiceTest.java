package net.thumbtack.school.notes.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.impl.DebugService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceTest {
    User registeredUser;
    String testSessionId;

    @Autowired
    DebugService debugService;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() throws NoteServerException {
        debugService.clearDatabase();
        registeredUser = debugService.registerUser();
        testSessionId = debugService.loginUser(registeredUser.getId());
    }

    @Test
    public void testUpdateUserInfo_rightParameters() throws NoteServerException {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest(
                "NewFirstName",
                "NewLastName",
                null,
                registeredUser.getPassword(),
                "new_good_password");
        userService.updateUserInfo(request, testSessionId);

        User resultUser = userService.getUserInfo(testSessionId);

        assertAll(
                () -> assertEquals(request.getFirstName(), resultUser.getFirstName()),
                () -> assertEquals(request.getLastName(), resultUser.getLastName()),
                () -> assertEquals(registeredUser.getLogin(), resultUser.getLogin())
        );
    }

    @Test
    public void testGiveUserAdminRoot_withRoot() throws NoteServerException {
        User testUser = debugService.registerUser();
        User admin = debugService.makeAdmin(testUser);
        String adminSession = debugService.loginUser(admin.getId());
        RegisterRequest newRequest = new RegisterRequest(
                "Test2",
                "Testov2",
                "Testovitch2",
                "login2",
                "test_password2");
        String testSessionIdSecond = UUID.randomUUID().toString();
        User user = userService.registerUser(newRequest, testSessionIdSecond);
        userService.makeAdmin(user.getId(), adminSession);
        User resultUser = userService.getUserInfo(testSessionIdSecond);

        assertAll(
                () -> assertEquals(UserStatus.ADMIN, resultUser.getUserStatus()),
                () -> assertEquals(newRequest.getLogin(), resultUser.getLogin())
        );
    }

    @Test
    public void testGiveUserAdminRoot_withoutRoot() throws NoteServerException {
        RegisterRequest newRequest = new RegisterRequest(
                "Test2",
                "Testov2",
                "Testovitch2",
                "login2",
                "test_password2");
        String testSessionIdSecond = UUID.randomUUID().toString();
        User user = userService.registerUser(newRequest, testSessionIdSecond);

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            userService.makeAdmin(user.getId(), testSessionId);
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("Not enough rights for this action"))
        );
    }

    @Test
    public void testLeaveServer_rightParameters() throws NoteServerException {
        LeaveServerRequest leaveRequest = new LeaveServerRequest(registeredUser.getPassword());
        userService.leaveServer(leaveRequest, testSessionId);

        User user = debugService.getUserAccountInfoByLogin(registeredUser.getLogin());

        assertAll(
                () -> assertNotNull(user),
                () -> assertTrue(user.isDeleted())
        );
    }

    @Test
    public void testLeaveServer_wrongPassword() throws NoteServerException {
        LeaveServerRequest leaveRequest = new LeaveServerRequest("wrong_password");

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            userService.leaveServer(leaveRequest, testSessionId);
        });
        User user = debugService.getUserAccountInfoByLogin(registeredUser.getLogin());

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertFalse(user.isDeleted()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString().contains("Wrong password"))
        );
    }


    @Test
    public void testLeaveServer_loggedOut() throws NoteServerException {
        userService.logoutUser(testSessionId);
        LeaveServerRequest leaveRequest = new LeaveServerRequest(registeredUser.getPassword());

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            userService.leaveServer(leaveRequest, testSessionId);
        });
        User user = debugService.getUserAccountInfoByLogin(registeredUser.getLogin());

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertFalse(user.isDeleted()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString().contains("No such session on the server"))
        );
    }
}
