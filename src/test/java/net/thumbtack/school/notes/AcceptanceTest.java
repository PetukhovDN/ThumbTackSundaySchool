package net.thumbtack.school.notes;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.comment.CommentRequest;
import net.thumbtack.school.notes.dto.request.comment.EditCommentRequest;
import net.thumbtack.school.notes.dto.request.comment.RateNoteRequest;
import net.thumbtack.school.notes.dto.request.note.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.request.user.FollowIgnoreRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentResponse;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
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

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcceptanceTest {
    final RestTemplate template = new RestTemplate();
    final String userUrl = "http://localhost:8888/api/";

    final RegisterRequest rightRegisterRequest = new RegisterRequest();
    final SectionRequest rightCreateSectionRequest = new SectionRequest();
    final NoteRequest rightCreateNoteRequest = new NoteRequest();
    final CommentRequest rightCreateCommentRequest = new CommentRequest();


    @BeforeEach
    void setUp() {
        template.postForObject(userUrl + "debug/clear", "", String.class);

        rightRegisterRequest.setFirstName("Test");
        rightRegisterRequest.setLastName("Testov");
        rightRegisterRequest.setPatronymic("Testovitch");
        rightRegisterRequest.setLogin("firstUserLogin");
        rightRegisterRequest.setPassword("good_password");

        rightCreateSectionRequest.setSectionName("TestSectionName");

        rightCreateNoteRequest.setSubject("TestNoteSubject");
        rightCreateNoteRequest.setBody("TestNoteBody");

        rightCreateCommentRequest.setBody("TestCommentBody");
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

    // tests for user accounts

    @Test
    public void testPost_rightParameters() {
        ResponseEntity<UserInfoResponse> response = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertNotNull(Objects.requireNonNull(response.getBody()).getFirstName()),
                () -> assertTrue(response.getHeaders().containsKey("Set-Cookie")),
                () -> assertEquals(rightRegisterRequest.getFirstName(), response.getBody().getFirstName())
        );
    }

    @Test
    public void testPost_wrongParameters() {
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
    public void getUserInfo_loggedIn() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();

        ResponseEntity<UserInfoResponse> response = template.exchange(
                userUrl + "account", HttpMethod.GET, new HttpEntity<>(headers),
                UserInfoResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertNotNull(response.getBody().getFirstName()),
                () -> assertEquals(rightRegisterRequest.getFirstName(), response.getBody().getFirstName())
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
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
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
                () -> assertEquals("NewFirstName", response.getBody().getFirstName()),
                () -> assertEquals("NewLastName", response.getBody().getLastName()),
                () -> assertEquals(rightRegisterRequest.getLogin(), response.getBody().getLogin())
        );
    }

    @Test
    public void updateUserInfo_wrongPassword() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        UpdateUserInfoRequest updatedRequest = new UpdateUserInfoRequest(
                "NewFirstName",
                "NewLastName",
                "",
                "wrong_old_password",
                "new_good_password");
        HttpEntity<UpdateUserInfoRequest> entity = new HttpEntity<>(updatedRequest, headers);

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
        headers.set("Cookie", makeAdminDebugResponse.getHeaders().getFirst("Set-Cookie"));
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
    public void testMakeAdmin_notEnoughRights() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        String registeredUserLogin = rightRegisterRequest.getLogin();
        HttpEntity<User> userResponse = template.getForEntity(userUrl + "debug/" + registeredUserLogin + "/get", User.class);
        int userId = userResponse.getBody().getId();
        HttpEntity<UpdateUserInfoRequest> entity = new HttpEntity<>(headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "accounts/" + userId + "/super", HttpMethod.PUT, entity, Void.class);
        });

        HttpEntity<User> adminResponse = template.getForEntity(userUrl + "debug/" + registeredUserLogin + "/get", User.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertNotNull(userResponse.getBody().getFirstName()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Not enough rights for this action")),
                () -> assertEquals(UserStatus.USER, userResponse.getBody().getUserStatus()),
                () -> assertEquals(UserStatus.USER, adminResponse.getBody().getUserStatus())
        );
    }

    @Test
    public void tesFollowAndIgnoreUser_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        FollowIgnoreRequest request = new FollowIgnoreRequest(rightRegisterRequest.getLogin());
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

    // tests for sessions

    @Test
    public void testLogin_alreadyLoggedIn() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();

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
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();

        LoginRequest loginRequest = new LoginRequest(
                "wrong login",
                "wrongpass");
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "sessions", HttpMethod.POST, entity, Void.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such user on the server"))
        );
    }

    // tests for sections

    @Test
    public void testCreateSection_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        SectionRequest createRequest = new SectionRequest("TestSection");
        HttpEntity<SectionRequest> createEntity = new HttpEntity<>(createRequest, headers);
        ResponseEntity<SectionResponse> createSectionResponse = template.exchange(
                userUrl + "sections", HttpMethod.POST, createEntity, SectionResponse.class);

        assertAll(
                () -> assertEquals(200, createSectionResponse.getStatusCodeValue()),
                () -> assertNotNull(createSectionResponse.getBody().getSectionName()),
                () -> assertEquals(createRequest.getSectionName(), createSectionResponse.getBody().getSectionName())
        );
    }

    @Test
    public void testCreateSection_incorrectSectionName() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        SectionRequest createRequest = new SectionRequest("WrongSectionName_!@#$%^&*()");
        HttpEntity<SectionRequest> createEntity = new HttpEntity<>(createRequest, headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "sections", HttpMethod.POST, createEntity, SectionResponse.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("Invalid section name"))
        );
    }

    @Test
    public void testGetSectionInfo_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        ResponseEntity<SectionResponse> createSectionResponse = createNewSectionForTests(headers);

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
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        HttpEntity<Void> getSectionEntity = new HttpEntity<>(headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "sections/" + 77,
                    HttpMethod.GET, getSectionEntity, Void.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such section on the server"))
        );
    }

    @Test
    public void testRenameSection_wrongSectionName() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        ResponseEntity<SectionResponse> createSectionResponse = createNewSectionForTests(headers);

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

    @Test
    public void testDeleteSection_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        ResponseEntity<SectionResponse> createSectionResponse = createNewSectionForTests(headers);
        HttpEntity<Void> deleteEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteSectionResponse = template.exchange(userUrl + "sections/" + createSectionResponse.getBody().getId(),
                HttpMethod.DELETE, deleteEntity, Void.class);

        assertAll(
                () -> assertEquals(200, deleteSectionResponse.getStatusCodeValue())
        );

    }

    @Test
    public void testDeleteSection_notExistingSection() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        ResponseEntity<SectionResponse> createSectionResponse = createNewSectionForTests(headers);
        HttpEntity<Void> headersEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteSectionResponse = template.exchange(
                userUrl + "sections/" + createSectionResponse.getBody().getId(),
                HttpMethod.DELETE, headersEntity, Void.class);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(
                    userUrl + "sections/" + createSectionResponse.getBody().getId(),
                    HttpMethod.GET, headersEntity, Void.class);
        });

        assertAll(
                () -> assertEquals(200, deleteSectionResponse.getStatusCodeValue()),
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such section on the server"))
        );
    }

    @Test
    public void testGetAllSectionsInfo_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        createNewSectionForTests(headers);
        rightCreateSectionRequest.setSectionName("SecondTestSectionName");
        createNewSectionForTests(headers);
        rightCreateSectionRequest.setSectionName("ThirdTestSectionName");
        createNewSectionForTests(headers);

        HttpEntity<Void> getSectionsEntity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> getSectionInfoResponse = template.exchange(userUrl + "sections",
                HttpMethod.GET, getSectionsEntity, ArrayList.class);

        assertAll(
                () -> assertEquals(200, getSectionInfoResponse.getStatusCodeValue()),
                () -> assertEquals(3, getSectionInfoResponse.getBody().size()),
                () -> assertNotNull(getSectionInfoResponse.getBody().get(0))
        );
    }

    // tests for notes

    @Test
    public void testCreateNote_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        NoteRequest createRequest = new NoteRequest(
                "NoteSubject",
                "NoteBody",
                sectionId);
        HttpEntity<NoteRequest> createEntity = new HttpEntity<>(createRequest, headers);
        ResponseEntity<NoteResponse> createNoteResponse = template.exchange(
                userUrl + "notes", HttpMethod.POST, createEntity, NoteResponse.class);

        assertAll(
                () -> assertEquals(200, createNoteResponse.getStatusCodeValue()),
                () -> assertEquals(createRequest.getSubject(), createNoteResponse.getBody().getSubject()),
                () -> assertEquals(createRequest.getBody(), createNoteResponse.getBody().getBody()),
                () -> assertEquals(sectionId, createNoteResponse.getBody().getSectionId())
        );
    }

    @Test
    public void testCreateNote_incorrectNoteInfo() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        NoteRequest createRequest = new NoteRequest(
                null,
                null,
                sectionId);
        HttpEntity<NoteRequest> createEntity = new HttpEntity<>(createRequest, headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "notes", HttpMethod.POST, createEntity, SectionResponse.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("subject")),
                () -> assertTrue(exc.getResponseBodyAsString().contains("body")),
                () -> assertTrue(exc.getResponseBodyAsString().contains("не должно равняться null"))
        );
    }

    @Test
    public void testGetNoteInfo_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        ResponseEntity<NoteResponse> createdNote = createNewNoteForTests(headers);
        HttpEntity<Void> getNoteEntity = new HttpEntity<>(headers);

        ResponseEntity<NoteResponse> getNoteInfoResponse = template.exchange(
                userUrl + "notes/" + createdNote.getBody().getId(), HttpMethod.GET, getNoteEntity, NoteResponse.class);

        assertAll(
                () -> assertEquals(200, getNoteInfoResponse.getStatusCodeValue()),
                () -> assertEquals(createdNote.getBody().getSubject(), getNoteInfoResponse.getBody().getSubject()),
                () -> assertEquals(createdNote.getBody().getBody(), getNoteInfoResponse.getBody().getBody()),
                () -> assertEquals(sectionId, getNoteInfoResponse.getBody().getSectionId())
        );
    }

    @Test
    public void testGetNoteInfo_notExistingNote() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        HttpEntity<Void> getNoteEntity = new HttpEntity<>(headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(
                    userUrl + "notes/" + 77, HttpMethod.GET, getNoteEntity, NoteResponse.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such note on the server"))
        );
    }

    @Test
    public void testEditNoteInfo_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        ResponseEntity<NoteResponse> createdNote = createNewNoteForTests(headers);
        EditNoteRequest editNoteRequest = new EditNoteRequest(
                "NewNoteBody",
                String.valueOf(sectionId)
        );
        HttpEntity<EditNoteRequest> getNoteEntity = new HttpEntity<>(editNoteRequest, headers);

        ResponseEntity<NoteResponse> editNoteResponse = template.exchange(
                userUrl + "notes/" + createdNote.getBody().getId(), HttpMethod.PUT, getNoteEntity, NoteResponse.class);

        assertAll(
                () -> assertEquals(200, editNoteResponse.getStatusCodeValue()),
                () -> assertEquals(createdNote.getBody().getSubject(), editNoteResponse.getBody().getSubject()),
                () -> assertEquals(editNoteRequest.getBody(), editNoteResponse.getBody().getBody()),
                () -> assertEquals(sectionId, editNoteResponse.getBody().getSectionId())
        );
    }

    @Test
    public void testDeleteNote_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        ResponseEntity<NoteResponse> createdNote = createNewNoteForTests(headers);

        HttpEntity<Void> noteEntity = new HttpEntity<>(headers);
        template.exchange(userUrl + "notes/" + createdNote.getBody().getId(), HttpMethod.DELETE, noteEntity, Void.class);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(
                    userUrl + "notes/" + createdNote.getBody().getId(), HttpMethod.GET, noteEntity, NoteResponse.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such note on the server"))
        );
    }

    @Test
    public void testRateNote_rightParameters() {
        MultiValueMap<String, String> firstUserHeaders = registerUserReturnHeaderWithSessionId();
        rightRegisterRequest.setLogin("anotherLogin");
        MultiValueMap<String, String> anotherUserHeaders = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(firstUserHeaders).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        ResponseEntity<NoteResponse> createdNote = createNewNoteForTests(firstUserHeaders);
        RateNoteRequest rateNoteRequest = new RateNoteRequest(
                "2"
        );
        HttpEntity<RateNoteRequest> getNoteEntity = new HttpEntity<>(rateNoteRequest, anotherUserHeaders);

        ResponseEntity<Void> rateNoteResponse = template.exchange(
                userUrl + "notes/" + createdNote.getBody().getId() + "/rating",
                HttpMethod.POST, getNoteEntity, Void.class);

        assertAll(
                () -> assertEquals(200, rateNoteResponse.getStatusCodeValue())
        );
    }

    @Test
    public void testRateNote_authorOfNote() {
        MultiValueMap<String, String> firstUserHeaders = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(firstUserHeaders).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        ResponseEntity<NoteResponse> createdNote = createNewNoteForTests(firstUserHeaders);
        RateNoteRequest rateNoteRequest = new RateNoteRequest(
                "2"
        );
        HttpEntity<RateNoteRequest> rateNoteEntity = new HttpEntity<>(rateNoteRequest, firstUserHeaders);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "notes/" + createdNote.getBody().getId() + "/rating",
                    HttpMethod.POST, rateNoteEntity, Void.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("You can`t rate note that you are the author of"))
        );
    }

    @Test
    public void testRateNote_wrongRating() {
        MultiValueMap<String, String> firstUserHeaders = registerUserReturnHeaderWithSessionId();
        rightRegisterRequest.setLogin("anotherLogin");
        MultiValueMap<String, String> anotherUserHeaders = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(firstUserHeaders).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        ResponseEntity<NoteResponse> createdNote = createNewNoteForTests(firstUserHeaders);
        RateNoteRequest rateNoteRequest = new RateNoteRequest();

        rateNoteRequest.setRating("qwerty");
        HttpEntity<RateNoteRequest> rateEntityRatingIsAString = new HttpEntity<>(rateNoteRequest, anotherUserHeaders);

        HttpClientErrorException excRatingIsAString = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "notes/" + createdNote.getBody().getId() + "/rating",
                    HttpMethod.POST, rateEntityRatingIsAString, Void.class);
        });

        assertAll(
                () -> assertEquals(400, excRatingIsAString.getStatusCode().value()),
                () -> assertTrue(excRatingIsAString.getResponseBodyAsString().contains("Rating must be a number between 1 and 5"))
        );

        rateNoteRequest.setRating("qwerty");
        HttpEntity<RateNoteRequest> rateEntityIncorrectRating = new HttpEntity<>(rateNoteRequest, anotherUserHeaders);

        HttpClientErrorException excIncorrectRatingNumber = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "notes/" + createdNote.getBody().getId() + "/rating",
                    HttpMethod.POST, rateEntityIncorrectRating, Void.class);
        });

        assertAll(
                () -> assertEquals(400, excIncorrectRatingNumber.getStatusCode().value()),
                () -> assertTrue(excIncorrectRatingNumber.getResponseBodyAsString().contains("Rating must be a number between 1 and 5"))
        );
    }

    // tests for comments

    @Test
    public void testCreateComment_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        CommentRequest createRequest = new CommentRequest(
                "CommentBody",
                noteId);
        HttpEntity<CommentRequest> createEntity = new HttpEntity<>(createRequest, headers);
        ResponseEntity<CommentResponse> createCommentResponse = template.exchange(
                userUrl + "comments", HttpMethod.POST, createEntity, CommentResponse.class);

        assertAll(
                () -> assertEquals(200, createCommentResponse.getStatusCodeValue()),
                () -> assertEquals(createRequest.getBody(), createCommentResponse.getBody().getBody()),
                () -> assertEquals(noteId, createCommentResponse.getBody().getNoteId())
        );
    }

    @Test
    public void testCreateComment_incorrectCommentBody() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        CommentRequest createRequest = new CommentRequest(
                "",
                noteId);
        HttpEntity<CommentRequest> createEntity = new HttpEntity<>(createRequest, headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(
                    userUrl + "comments", HttpMethod.POST, createEntity, CommentResponse.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("body")),
                () -> assertTrue(exc.getResponseBodyAsString().contains("не должно быть пустым"))
        );
    }

    @Test
    public void testGetAllNoteComments_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        rightCreateCommentRequest.setNoteId(noteId);
        createNewCommentForTests(headers);
        createNewCommentForTests(headers);
        createNewCommentForTests(headers);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> getAllNoteComments = template.exchange(
                userUrl + "notes/" + noteId + "/comments", HttpMethod.GET, getEntity, ArrayList.class);

        assertAll(
                () -> assertEquals(200, getAllNoteComments.getStatusCodeValue()),
                () -> assertEquals(3, getAllNoteComments.getBody().size())
        );
    }

    @Test
    public void testGetAllNoteComments_notExistingNote() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        rightCreateCommentRequest.setNoteId(noteId);
        createNewCommentForTests(headers);
        createNewCommentForTests(headers);
        createNewCommentForTests(headers);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "notes/" + 77 + "/comments", HttpMethod.GET, getEntity, ArrayList.class);
        });

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(exc.getResponseBodyAsString().contains("No such note on the server"))
        );
    }

    @Test
    public void testEditComment_editCommentInfo() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        rightCreateCommentRequest.setNoteId(noteId);
        ResponseEntity<CommentResponse> createCommentResponse = createNewCommentForTests(headers);
        EditCommentRequest editCommentRequest = new EditCommentRequest(
                "new comment body"
        );
        HttpEntity<EditCommentRequest> editEntity = new HttpEntity<>(editCommentRequest, headers);

        ResponseEntity<CommentResponse> editCommentResponse = template.exchange(
                userUrl + "comments/" + createCommentResponse.getBody().getId(),
                HttpMethod.PUT, editEntity, CommentResponse.class);

        assertAll(
                () -> assertEquals(200, editCommentResponse.getStatusCodeValue()),
                () -> assertEquals(createCommentResponse.getBody().getId(), editCommentResponse.getBody().getId()),
                () -> assertEquals("new comment body", editCommentResponse.getBody().getBody())
        );
    }


    @Test
    public void testEditComment_wrongParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        rightCreateCommentRequest.setNoteId(noteId);
        EditCommentRequest editCommentRequest = new EditCommentRequest(
                "new comment body"
        );
        HttpEntity<EditCommentRequest> editEntity = new HttpEntity<>(editCommentRequest, headers);

        HttpClientErrorException excNoCommentOnTheServer = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "comments/" + 77,
                    HttpMethod.PUT, editEntity, CommentResponse.class);
        });

        HttpClientErrorException excWrongCommentIdentifier = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(userUrl + "comments/" + "qwerty",
                    HttpMethod.PUT, editEntity, CommentResponse.class);
        });

        assertAll(
                () -> assertEquals(400, excNoCommentOnTheServer.getStatusCode().value()),
                () -> assertTrue(excNoCommentOnTheServer.getResponseBodyAsString().contains("No such comment on the server")),
                () -> assertEquals(400, excWrongCommentIdentifier.getStatusCode().value()),
                () -> assertTrue(excWrongCommentIdentifier.getResponseBodyAsString().contains("Incorrect comment identifier"))

        );
    }

    @Test
    public void testDeleteComment_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        rightCreateCommentRequest.setNoteId(noteId);
        ResponseEntity<CommentResponse> createCommentResponse = createNewCommentForTests(headers);
        HttpEntity<Void> commentEntity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> getAllNoteCommentsBeforeDeleting = template.exchange(
                userUrl + "notes/" + noteId + "/comments", HttpMethod.GET, commentEntity, ArrayList.class);
        template.exchange(userUrl + "comments/" + createCommentResponse.getBody().getId(),
                HttpMethod.DELETE, commentEntity, Void.class);
        ResponseEntity<ArrayList> getAllNoteCommentsAfterDeleting = template.exchange(
                userUrl + "notes/" + noteId + "/comments", HttpMethod.GET, commentEntity, ArrayList.class);

        assertAll(
                () -> assertEquals(200, getAllNoteCommentsBeforeDeleting.getStatusCodeValue()),
                () -> assertEquals(200, getAllNoteCommentsAfterDeleting.getStatusCodeValue()),
                () -> assertEquals(1, getAllNoteCommentsBeforeDeleting.getBody().size()),
                () -> assertEquals(0, getAllNoteCommentsAfterDeleting.getBody().size())
        );
    }

    @Test
    public void testDeleteAllNoteComments_rightParameters() {
        MultiValueMap<String, String> headers = registerUserReturnHeaderWithSessionId();
        int sectionId = createNewSectionForTests(headers).getBody().getId();
        rightCreateNoteRequest.setSectionId(sectionId);
        int noteId = createNewNoteForTests(headers).getBody().getId();
        rightCreateCommentRequest.setNoteId(noteId);
        createNewCommentForTests(headers);
        createNewCommentForTests(headers);
        createNewCommentForTests(headers);
        HttpEntity<Void> commentEntity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> getAllNoteCommentsBeforeDeleting = template.exchange(
                userUrl + "notes/" + noteId + "/comments", HttpMethod.GET, commentEntity, ArrayList.class);
        template.exchange(userUrl + "notes/" + noteId + "/comments",
                HttpMethod.DELETE, commentEntity, Void.class);
        ResponseEntity<ArrayList> getAllNoteCommentsAfterDeleting = template.exchange(
                userUrl + "notes/" + noteId + "/comments", HttpMethod.GET, commentEntity, ArrayList.class);

        assertAll(
                () -> assertEquals(200, getAllNoteCommentsBeforeDeleting.getStatusCodeValue()),
                () -> assertEquals(200, getAllNoteCommentsAfterDeleting.getStatusCodeValue()),
                () -> assertEquals(3, getAllNoteCommentsBeforeDeleting.getBody().size()),
                () -> assertEquals(0, getAllNoteCommentsAfterDeleting.getBody().size())
        );
    }

    /**
     * Registers new user to the server using information from user registration request
     * User registration request is set in @SetUp method
     *
     * @return ResponseEntity which contains information of registered user and headers
     */
    public MultiValueMap<String, String> registerUserReturnHeaderWithSessionId() {
        HttpEntity<UserInfoResponse> registerResponse = template.postForEntity(userUrl + "accounts", rightRegisterRequest, UserInfoResponse.class);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", registerResponse.getHeaders().getFirst("Set-Cookie"));
        return headers;
    }

    /**
     * Creates new section on the server, using session token of current user
     * Section creation  request is set in @SetUp method and contains new section name
     *
     * @param headers contains user cookie - session token
     * @return ResponseEntity which contains information of created section
     */
    public ResponseEntity<SectionResponse> createNewSectionForTests(MultiValueMap<String, String> headers) {
        HttpEntity<SectionRequest> createEntity = new HttpEntity<>(rightCreateSectionRequest, headers);
        return template.exchange(
                userUrl + "sections", HttpMethod.POST, createEntity, SectionResponse.class);
    }

    /**
     * Creates new note on the server, using session token of current user
     * Section identifier for note is set in every method into the note creation request, which is defined in @SetUp method
     *
     * @param headers contains user cookie - session token
     * @return ResponseEntity which contains information of created note
     */
    public ResponseEntity<NoteResponse> createNewNoteForTests(MultiValueMap<String, String> headers) {
        HttpEntity<NoteRequest> createEntity = new HttpEntity<>(rightCreateNoteRequest, headers);
        return template.exchange(
                userUrl + "notes", HttpMethod.POST, createEntity, NoteResponse.class);
    }

    /**
     * Creates new comment on the server, using session token of current user
     * Note identifier for comment is set in every method into the comment creation request, which is defined in @SetUp method
     *
     * @param headers contains user cookie - session token
     * @return ResponseEntity which contains information of created comment
     */
    public ResponseEntity<CommentResponse> createNewCommentForTests(MultiValueMap<String, String> headers) {
        HttpEntity<CommentRequest> createEntity = new HttpEntity<>(rightCreateCommentRequest, headers);
        return template.exchange(
                userUrl + "comments", HttpMethod.POST, createEntity, CommentResponse.class);
    }
}
