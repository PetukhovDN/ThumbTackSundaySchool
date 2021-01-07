package net.thumbtack.school.notes;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.request.user.FollowIgnoreRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
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
    final RestTemplate template = new RestTemplate();
    final RegisterRequest rightRegisterRequest = new RegisterRequest();
    final String userUrl = "http://localhost:8888/api/";

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
        ResponseEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));
        LoginRequest loginRequest = new LoginRequest(
                rightRegisterRequest.getLogin(),
                rightRegisterRequest.getPassword());
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<Void> response = template.exchange(
                userUrl + "sessions", HttpMethod.POST, entity,
                Void.class);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertTrue(response.getHeaders().containsKey("Set-Cookie"))
        );

    }

    @Test
    public void testLoginWithWrongParameters() {
        ResponseEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));
        LoginRequest loginRequest = new LoginRequest(
                "wrong login",
                "wrongpass");
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "sessions", HttpMethod.POST, entity, Void.class);
        });
        System.out.println(exc.getResponseBodyAsString());
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such user on the server"))
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
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such session on the server"))
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
    public void testMakeAdmin_rightParameters() {
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

    @Test
    public void tesFollowAndIgnoreUser_rightParameters() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));

        FollowIgnoreRequest request = new FollowIgnoreRequest(registerResponse.getBody().getLogin());
        HttpEntity<FollowIgnoreRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Void> followResponse = template.exchange(
                userUrl + "followings", HttpMethod.POST, entity, Void.class);

        ResponseEntity<Void> ignoreResponse = template.exchange(
                userUrl + "ignore", HttpMethod.POST, entity, Void.class);

        assertAll(
                () -> assertEquals(200, followResponse.getStatusCodeValue()),
                () -> assertEquals(200, ignoreResponse.getStatusCodeValue())
        );
    }

    @Test
    public void testGetSectionInfoSection_rightParameters() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));

        SectionRequest createRequest = new SectionRequest("TestSection");
        HttpEntity<SectionRequest> createEntity = new HttpEntity<>(createRequest, headers);
        ResponseEntity<SectionResponse> createSectionResponse = template.exchange(
                userUrl + "sections", HttpMethod.POST, createEntity, SectionResponse.class);

        HttpEntity<Void> getSectionEntity = new HttpEntity<>(headers);
        ResponseEntity<SectionResponse> getSectionInfoResponse = template.exchange(userUrl + "sections/" + createSectionResponse.getBody().getId(),
                HttpMethod.GET, getSectionEntity, SectionResponse.class);

        assertAll(
                () -> assertEquals(200, getSectionInfoResponse.getStatusCodeValue()),
                () -> assertEquals(createSectionResponse.getBody().getId(), getSectionInfoResponse.getBody().getId()),
                () -> assertEquals(createSectionResponse.getBody().getSectionName(), getSectionInfoResponse.getBody().getSectionName())
        );
    }

    @Test
    public void testGetSectionInfo_notExistingSection() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));

        SectionRequest createRequest = new SectionRequest("TestSection");
        HttpEntity<SectionRequest> createEntity = new HttpEntity<>(createRequest, headers);
        template.exchange(userUrl + "sections", HttpMethod.POST, createEntity, SectionResponse.class);

        HttpEntity<Void> getSectionEntity = new HttpEntity<>(headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "sections/" + 77,
                    HttpMethod.GET, getSectionEntity, Void.class);
        });
        System.out.println(exc.getResponseBodyAsString());

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such section on the server"))
        );
    }

    @Test
    public void testRenameSection_wrongSectionName() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));

        SectionRequest createRequest = new SectionRequest("TestSection");
        HttpEntity<SectionRequest> createEntity = new HttpEntity<>(createRequest, headers);
        ResponseEntity<SectionResponse> createSectionResponse = template.exchange(
                userUrl + "sections", HttpMethod.POST, createEntity, SectionResponse.class);

        SectionRequest renameRequest = new SectionRequest("WrongSectionName_!@#$%^&*()");
        HttpEntity<SectionRequest> renameEntity = new HttpEntity<>(renameRequest, headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "sections/" + createSectionResponse.getBody().getId(),
                    HttpMethod.PUT, renameEntity, SectionResponse.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid section name"))
        );
    }

}
