package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UsersInfoResponse;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


class UserMapStructTest {
    UserMapStruct INSTANCE = Mappers.getMapper(UserMapStruct.class);

    @Test
    void testTransformRegisterUserRequestToUserModel() {
        RegisterRequest registerRequest = new RegisterRequest(
                "firstName",
                "lastName",
                "patronymic",
                "login",
                "good_password"
        );
        User resultUser = INSTANCE.requestRegisterUser(registerRequest);

        assertAll(
                () -> assertEquals(registerRequest.getFirstName(), resultUser.getFirstName()),
                () -> assertEquals(registerRequest.getLastName(), resultUser.getLastName()),
                () -> assertEquals(registerRequest.getPatronymic(), resultUser.getPatronymic()),
                () -> assertEquals(registerRequest.getLogin(), resultUser.getLogin()),
                () -> assertEquals(registerRequest.getPassword(), resultUser.getPassword())
        );
    }

    @Test
    void testTransformUserModelToRegisterUserResponse() {
        User resultUser = new User();
        resultUser.setFirstName("firstName");
        resultUser.setLastName("lastName");
        resultUser.setPatronymic("patronymic");
        resultUser.setLogin("login");
        UserInfoResponse userInfoResponse = INSTANCE.responseRegisterUser(resultUser);

        assertAll(
                () -> assertEquals(resultUser.getFirstName(), userInfoResponse.getFirstName()),
                () -> assertEquals(resultUser.getLastName(), userInfoResponse.getLastName()),
                () -> assertEquals(resultUser.getPatronymic(), userInfoResponse.getPatronymic()),
                () -> assertEquals(resultUser.getLogin(), userInfoResponse.getLogin())
        );
    }

    @Test
    void testTransformUpdateUserRequestToUserModel() {
        UpdateUserInfoRequest registerRequest = new UpdateUserInfoRequest(
                "firstName",
                "lastName",
                "patronymic",
                "old_password",
                "new_password");
        User resultUser = INSTANCE.requestUpdateUser(registerRequest);

        assertAll(
                () -> assertEquals(registerRequest.getFirstName(), resultUser.getFirstName()),
                () -> assertEquals(registerRequest.getLastName(), resultUser.getLastName()),
                () -> assertEquals(registerRequest.getPatronymic(), resultUser.getPatronymic()),
                () -> assertEquals(registerRequest.getNewPassword(), resultUser.getPassword())
        );
    }

    @Test
    void testTransformUserModelToUpdateUserResponse() {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setFirstName("firstName");
        resultUser.setLastName("lastName");
        resultUser.setPatronymic("patronymic");
        resultUser.setLogin("login");
        UpdateUserInfoResponse userInfoResponse = INSTANCE.responseUpdateUserInfo(resultUser);

        assertAll(
                () -> assertEquals(resultUser.getId(), userInfoResponse.getId()),
                () -> assertEquals(resultUser.getFirstName(), userInfoResponse.getFirstName()),
                () -> assertEquals(resultUser.getLastName(), userInfoResponse.getLastName()),
                () -> assertEquals(resultUser.getPatronymic(), userInfoResponse.getPatronymic()),
                () -> assertEquals(resultUser.getLogin(), userInfoResponse.getLogin())
        );
    }

    @Test
    void testTransformUserModelToUsersInfoResponse() {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setFirstName("firstName");
        resultUser.setLastName("lastName");
        resultUser.setPatronymic("patronymic");
        resultUser.setLogin("login");
        resultUser.setCreationTime(LocalDateTime.now());
        resultUser.setOnline(true);
        resultUser.setDeleted(true);
        resultUser.setUserStatus(UserStatus.ADMIN);
        UsersInfoResponse userInfoResponse = INSTANCE.responseGetAllUsers(resultUser);

        assertAll(
                () -> assertEquals(resultUser.getId(), userInfoResponse.getId()),
                () -> assertEquals(resultUser.getFirstName(), userInfoResponse.getFirstName()),
                () -> assertEquals(resultUser.getLastName(), userInfoResponse.getLastName()),
                () -> assertEquals(resultUser.getPatronymic(), userInfoResponse.getPatronymic()),
                () -> assertEquals(resultUser.getLogin(), userInfoResponse.getLogin()),
                () -> assertEquals(resultUser.getCreationTime(), userInfoResponse.getCreationTime()),
                () -> assertEquals(resultUser.isOnline(), userInfoResponse.isOnline()),
                () -> assertEquals(resultUser.isDeleted(), userInfoResponse.isDeleted()),
                () -> assertEquals(resultUser.getUserStatus(), userInfoResponse.getUserStatus())
        );
    }
}