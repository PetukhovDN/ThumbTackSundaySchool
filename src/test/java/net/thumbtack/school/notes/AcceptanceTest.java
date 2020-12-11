package net.thumbtack.school.notes;

import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {
    private RestTemplate template = new RestTemplate();
    private RegisterRequest rightRegisterRequest = new RegisterRequest();
    private String postUserUrl = "http://localhost:8888/api/";

    @BeforeEach
    void setUp() {
        template.postForObject(postUserUrl + "debug/clear", "", String.class);
        rightRegisterRequest.setFirstName("Test");
        rightRegisterRequest.setLastName("Testov");
        rightRegisterRequest.setPatronymic("Testovitch");
        rightRegisterRequest.setLogin("test");
        rightRegisterRequest.setPassword("good_password");
    }

    @Test
    public void testPostWithRightParameters() {
        User actualUser = template.postForObject(postUserUrl + "accounts", rightRegisterRequest, User.class);
        assert actualUser != null;

        assertEquals(rightRegisterRequest.getFirstName(), actualUser.getFirstName());
    }

    @Test
    public void testPostWithWrongParameters() {
        rightRegisterRequest.setLogin(null);
        rightRegisterRequest.setPassword("short");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            User actualUser = template.postForObject(postUserUrl + "accounts", rightRegisterRequest, User.class);
        });
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid user password")),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid user login"))
        );
    }

    @Test
    public void testLoginWithRightParameters() {
        template.postForObject(postUserUrl + "accounts", rightRegisterRequest, User.class);
        // REVU а cookie проверить ?
        // https://codeflex.co/java-rest-client-post-with-cookie/
        // http://codeflex.co/java-rest-client-get-cookie/ 
        // https://stackoverflow.com/questions/24642508/spring-inserting-cookies-in-a-rest-call-response/54507027
        // https://dzone.com/articles/how-to-use-cookies-in-spring-boot

        LoginRequest loginRequest = new LoginRequest(
                rightRegisterRequest.getLogin(),
                rightRegisterRequest.getPassword());
        template.postForObject(postUserUrl + "sessions", loginRequest, String.class);

        //
    }

    @Test
    public void testLoginWithWrongParameters() {
        LoginRequest loginRequest = new LoginRequest(
                "wrong login",
                "wrongpass"
        );

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            User actualUser = template.postForObject(postUserUrl + "sessions", loginRequest, User.class);
        });
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid user password"))
        );
    }

    @Test
    public void testGetFromNotExistingPage() {
        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.getForEntity(postUserUrl + "wrong/page", String.class);
        });
        assertAll(
                () -> assertEquals(404, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Not Found"))
        );
    }
}
