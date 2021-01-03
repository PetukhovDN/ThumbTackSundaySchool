package net.thumbtack.school.notes;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcceptanceTest {
    RestTemplate template = new RestTemplate();
    RegisterRequest rightRegisterRequest = new RegisterRequest();
    String userUrl = "http://localhost:8888/api/";

    @BeforeEach
    void setUp() {
        template.postForObject(userUrl + "debug/clear", "", String.class);
        rightRegisterRequest.setFirstName("Test");
        rightRegisterRequest.setLastName("Testov");
        rightRegisterRequest.setPatronymic("Testovitch");
        rightRegisterRequest.setLogin("test");
        rightRegisterRequest.setPassword("good_password");
    }

    @Test
    public void testPostWithRightParameters() {
        ResponseEntity<UserInfoResponse> response = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertNotNull(Objects.requireNonNull(response.getBody()).getFirstName()),
                () -> assertTrue(response.getHeaders().containsKey("Set-Cookie")),
                () -> assertEquals(rightRegisterRequest.getFirstName(), response.getBody().getFirstName())
        );
    }

    @Test
    public void testPostWithWrongParameters() {
        rightRegisterRequest.setLogin(null);
        rightRegisterRequest.setPassword("short");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForObject(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);
        });
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid user password")),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid user login"))
        );
    }

    @Test
    public void testLogin_alreadyLoggedIn() {
        template.postForObject(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        LoginRequest loginRequest = new LoginRequest(
                rightRegisterRequest.getLogin(),
                rightRegisterRequest.getPassword());

        ResponseEntity<Void> response = template.postForEntity(userUrl + "sessions", loginRequest, Void.class);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertTrue(response.getHeaders().containsKey("Set-Cookie"))
        );

    }

    @Test
    public void testLoginWithWrongParameters() {
        LoginRequest loginRequest = new LoginRequest(
                "wrong login",
                "wrongpass"
        );

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForObject(userUrl + "sessions", loginRequest, Void.class);
        });
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid user password"))
        );
    }

    @Test
    public void testGetFromNotExistingPage() {
        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.getForEntity(userUrl + "wrong/page", Void.class);
        });
        assertAll(
                () -> assertEquals(404, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Not Found"))
        );
    }

    @Test
    public void getUserInfo_loggedIn() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));

        ResponseEntity<UserInfoResponse> response = template.exchange(
                userUrl + "account", HttpMethod.GET, new HttpEntity<>(headers),
                UserInfoResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertNotNull(response.getBody().getFirstName()),
                () -> assertNotNull(registerResponse.getBody().getFirstName()),
                () -> assertEquals(response.getBody().getFirstName(), registerResponse.getBody().getFirstName())
        );
    }

    @Test
    public void getUserInfo_notLoggedIn() {
        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.getForEntity(userUrl + "account", UserInfoResponse.class);
        });
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("You are not logged in"))
        );
    }

    @Test
    public void getUserInfo_wrongSessionId() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", "JAVASESSIONID=wrong_session_token");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "account", HttpMethod.GET, new HttpEntity<>(headers), UserInfoResponse.class);
        });
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such session on the server"))
        );
    }

    @Test
    public void updateUserInfo_rightParameters() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));
        UpdateUserInfoRequest request = new UpdateUserInfoRequest(
                "NewFirstName",
                "NewLastName",
                "",
                rightRegisterRequest.getPassword(),
                "new_good_password");
        HttpEntity<UpdateUserInfoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<UpdateUserInfoResponse> response = template.exchange(
                userUrl + "accounts", HttpMethod.PUT, entity,
                UpdateUserInfoResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertNotNull(response.getBody().getFirstName()),
                () -> assertNotNull(registerResponse.getBody().getFirstName()),
                () -> assertEquals("NewFirstName", response.getBody().getFirstName()),
                () -> assertEquals("NewLastName", response.getBody().getLastName()),
                () -> assertEquals(rightRegisterRequest.getLogin(), response.getBody().getLogin())
        );
    }

    @Test
    public void updateUserInfo_wrongPassword() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));
        UpdateUserInfoRequest request = new UpdateUserInfoRequest(
                "NewFirstName",
                "NewLastName",
                "",
                "wrong_old_password",
                "new_good_password");
        HttpEntity<UpdateUserInfoRequest> entity = new HttpEntity<>(request, headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "accounts", HttpMethod.PUT, entity, UpdateUserInfoResponse.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Wrong password"))
        );
    }

    @Test
    public void testMakeAdmin() {
        HttpEntity<Void> makeAdminDebugResponse = template.postForEntity(userUrl + "debug/super", "", Void.class);
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);
        String registeredUserLogin = registerResponse.getBody().getLogin();
        HttpEntity<User> userResponse = template.getForEntity(userUrl + "debug/" + registeredUserLogin + "/get", User.class);
        int userId = userResponse.getBody().getId();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", makeAdminDebugResponse.getHeaders().getFirst("Set-Cookie"));
        HttpEntity<UpdateUserInfoRequest> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> makeAdminResponse = template.exchange(userUrl + "accounts/" + userId + "/super", HttpMethod.PUT, entity, Void.class);

        HttpEntity<User> adminResponse = template.getForEntity(userUrl + "debug/" + registeredUserLogin + "/get", User.class);

        assertAll(
                () -> assertEquals(200, makeAdminResponse.getStatusCodeValue()),
                () -> assertNotNull(userResponse.getBody().getFirstName()),
                () -> assertEquals(UserStatus.USER, userResponse.getBody().getUserStatus()),
                () -> assertEquals(UserStatus.ADMIN, adminResponse.getBody().getUserStatus())
        );
    }

}
