package net.thumbtack.school.notes;

import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
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
public class AcceptanceTest {
    private RestTemplate template = new RestTemplate();
    private RegisterRequest rightRegisterRequest = new RegisterRequest();
    private String userUrl = "http://localhost:8888/api/";

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

}
