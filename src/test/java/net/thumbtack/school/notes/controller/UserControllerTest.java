package net.thumbtack.school.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.exceptions.GlobalErrorHandler;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserServiceImpl userService;

    @Test
    public void testRegisterUserRight() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "Test",
                "Testov",
                "Testovitch",
                "login",
                "good_password");
        MvcResult result = mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testRegisterUserInFail() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "Test",  //true
                "$@*(QWE", //false
                "", //true
                "user123 *&^",  //false
                "short"); //false
        MvcResult result = mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 400);
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);
        assertEquals(3, error.getErrors().size());
    }


    @Test
    public void testLogoutAndLoginRegisteredUserRight() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "Test",
                "Testov",
                "Testovitch",
                "login",
                "good_password");
        mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
                .andReturn();

        MvcResult resultOfLogout = mvc.perform(delete("/api/sessions")).andReturn();

        LoginRequest loginRequest = new LoginRequest(
                registerRequest.getLogin(),
                registerRequest.getPassword());
        MvcResult resultOfLogin = mvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andReturn();

        assertEquals(resultOfLogin.getResponse().getStatus(), 200);
    }
}