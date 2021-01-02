package net.thumbtack.school.notes.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.impl.DebugService;
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

        UserInfoResponse userInfo = userService.getUserInfo(testSessionId);

        assertAll(
                () -> assertEquals(request.getFirstName(), userInfo.getFirstName()),
                () -> assertEquals(request.getLastName(), userInfo.getLastName()),
                () -> assertEquals(rightRegisterRequest.getLogin(), userInfo.getLogin())
        );

    }
}
