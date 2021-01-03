package net.thumbtack.school.notes.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
    RegisterRequest rightRegisterRequest;
    String testSessionId;

    @Autowired
    DebugService debugService;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() {
        debugService.clearDatabase();
        rightRegisterRequest = new RegisterRequest(
                "Test",
                "Testov",
                "Testovitch",
                "login",
                "test_password");
        testSessionId = UUID.randomUUID().toString();
    }

    public String makeAdminForTests() {
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Adminov");
        admin.setLogin("admin");
        admin.setPassword("admin_password");
        admin.setUserStatus(UserStatus.ADMIN);
        return debugService.makeAdmin(admin);
    }

    @Test
    public void testUpdateUserInfo_rightParameters() throws NoteServerException {
        userService.registerUser(rightRegisterRequest, testSessionId);
        UpdateUserInfoRequest request = new UpdateUserInfoRequest(
                "NewFirstName",
                "NewLastName",
                null,
                rightRegisterRequest.getPassword(),
                "new_good_password");
        userService.updateUserInfo(request, testSessionId);

        User resultUser = userService.getUserInfo(testSessionId);

        assertAll(
                () -> assertEquals(request.getFirstName(), resultUser.getFirstName()),
                () -> assertEquals(request.getLastName(), resultUser.getLastName()),
                () -> assertEquals(rightRegisterRequest.getLogin(), resultUser.getLogin())
        );
    }

    @Test
    public void testGiveUserAdminRoot_withRoot() throws NoteServerException {
        String adminSession = makeAdminForTests();
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
        userService.registerUser(rightRegisterRequest, testSessionId);
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


}
