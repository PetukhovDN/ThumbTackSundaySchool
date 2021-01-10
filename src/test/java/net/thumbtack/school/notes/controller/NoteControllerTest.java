package net.thumbtack.school.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.comment.CommentRequest;
import net.thumbtack.school.notes.dto.request.note.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.exceptions.GlobalErrorHandler;
import net.thumbtack.school.notes.service.impl.CommentServiceImpl;
import net.thumbtack.school.notes.service.impl.NoteServiceImpl;
import net.thumbtack.school.notes.service.impl.SectionServiceImpl;
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

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = NoteController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteControllerTest {
    Cookie testCookie;
    RegisterRequest registerRequest;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    NoteServiceImpl noteService;

    @MockBean
    SectionServiceImpl sectionService;

    @MockBean
    CommentServiceImpl commentService;

    @Test
    public void testCreateSection_rightParameters() throws Exception {
        SectionRequest createSectionRequest = new SectionRequest();
        createSectionRequest.setSectionName("NewSectionName");
        MvcResult result = mvc.perform(post("/api/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createSectionRequest)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testCreateSection_wrongSectionName() throws Exception {
        SectionRequest createSectionRequest = new SectionRequest();
        createSectionRequest.setSectionName("IncorrectSectionName+ !@#$%^&*()");
        MvcResult result = mvc.perform(post("/api/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createSectionRequest)))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);
        assertEquals(1, error.getErrors().size());
    }

    @Test
    public void testRenameSection_rightParameters() throws Exception {
        SectionRequest renameSectionRequest = new SectionRequest();
        renameSectionRequest.setSectionName("NewSectionName");
        MvcResult result = mvc.perform(put("/api/sections/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(renameSectionRequest)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testRenameSection_wrongSectionName() throws Exception {
        SectionRequest renameSectionRequest = new SectionRequest();
        renameSectionRequest.setSectionName("IncorrectSectionName+ !@#$%^&*()");
        MvcResult result = mvc.perform(put("/api/sections/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(renameSectionRequest)))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);
        assertEquals(1, error.getErrors().size());
    }

    @Test
    public void testDeleteSection_wrongSectionId() throws Exception {
        MvcResult result = mvc.perform(delete("/api/sections/qwerty")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);
        assertEquals(1, error.getErrors().size());
    }

    @Test
    public void testCreateNote_rightParameters() throws Exception {
        NoteRequest createNoteRequest = new NoteRequest(
                "noteSubject",
                "NoteBody",
                1
        );
        MvcResult result = mvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createNoteRequest)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testCreateNote_emptyNote() throws Exception {
        NoteRequest createNoteRequest = new NoteRequest(
                null,
                null,
                1
        );
        MvcResult result = mvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createNoteRequest)))
                .andReturn();
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertEquals(2, error.getErrors().size()),
                () -> assertTrue(error.getErrors().toString().contains("NotEmpty"))
        );
    }

    @Test
    public void testEditNote_rightParameters() throws Exception {
        EditNoteRequest editNoteRequest = new EditNoteRequest(
                "NoteBody",
                "1"
        );
        MvcResult result = mvc.perform(put("/api/notes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(editNoteRequest)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testEditNote_nullSectionId() throws Exception {
        EditNoteRequest editNoteRequest = new EditNoteRequest(
                "NoteBody",
                null
        );
        MvcResult result = mvc.perform(put("/api/notes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(editNoteRequest)))
                .andReturn();
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertEquals(1, error.getErrors().size()),
                () -> assertTrue(error.getErrors().toString().contains("NotNull"))
        );
    }

    @Test
    public void testAddComment_rightParameters() throws Exception {
        CommentRequest addCommentRequest = new CommentRequest(
                "CommentText",
                1
        );
        MvcResult result = mvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addCommentRequest)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testDeleteComment_rightParameters() throws Exception {
        MvcResult result = mvc.perform(delete("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testDeleteComment_wrongCommentId() throws Exception {
        MvcResult result = mvc.perform(delete("/api/comments/wrong_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);
        assertEquals(1, error.getErrors().size());
    }
}
