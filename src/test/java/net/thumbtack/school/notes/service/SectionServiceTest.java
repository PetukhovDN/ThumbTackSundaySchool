package net.thumbtack.school.notes.service;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.impl.DebugService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionServiceTest {
    User firstRegisteredUser;
    User secondRegisteredUser;
    String firstUserSessionId;
    String secondUserSessionId;

    @Autowired
    DebugService debugService;

    @Autowired
    SectionService sectionService;

    @BeforeEach
    public void setUp() {
        debugService.clearDatabase();
        firstRegisteredUser = debugService.registerUser();
        secondRegisteredUser = debugService.registerUser();
    }

    @Test
    public void testCreateSection_wrongSessionID() {
        firstUserSessionId = debugService.loginUser(firstRegisteredUser.getId());
        SectionRequest createRequest = new SectionRequest("TestSection");

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.createSection(createRequest, "wrong_session_id");
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such session on the server"))
        );
    }

    @Test
    public void testRenameSection_wrongSessionId() throws NoteServerException {
        firstUserSessionId = debugService.loginUser(firstRegisteredUser.getId());
        SectionRequest createRequest = new SectionRequest("TestSection");
        SectionResponse section = sectionService.createSection(createRequest, firstUserSessionId);

        SectionRequest renameRequest = new SectionRequest("TestSectionNewName");


        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.renameSection(renameRequest, "wrong_session_id", section.getId());
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such session on the server"))
        );
    }

    @Test
    public void testDeleteSection_notAuthorOfSection() throws NoteServerException {
        firstUserSessionId = debugService.loginUser(firstRegisteredUser.getId());
        secondUserSessionId = debugService.loginUser(secondRegisteredUser.getId());
        SectionRequest createRequest = new SectionRequest("TestSection");
        SectionResponse section = sectionService.createSection(createRequest, firstUserSessionId);

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.deleteSection(secondUserSessionId, section.getId());
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("You are not creator of this section"))
        );
    }

    @Test
    public void testGetSectionInfo_notExistingSection() {
        firstUserSessionId = debugService.loginUser(firstRegisteredUser.getId());
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.getSectionInfo(firstUserSessionId, 77);
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such section on the server"))
        );
    }

    @Test
    public void testGetAllSectionsInfo_wrongSessionId() throws NoteServerException {
        firstUserSessionId = debugService.loginUser(firstRegisteredUser.getId());
        sectionService.createSection(new SectionRequest("TestSection1"), firstUserSessionId);
        sectionService.createSection(new SectionRequest("TestSection2"), firstUserSessionId);
        sectionService.createSection(new SectionRequest("TestSection3"), firstUserSessionId);

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.getAllSections("wrong_session_id");
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such session on the server"))
        );
    }

    @Test
    public void testGetAllSectionsInfo_rightParameters() throws NoteServerException {
        firstUserSessionId = debugService.loginUser(firstRegisteredUser.getId());
        sectionService.createSection(new SectionRequest("TestSection1"), firstUserSessionId);
        sectionService.createSection(new SectionRequest("TestSection2"), firstUserSessionId);
        sectionService.createSection(new SectionRequest("TestSection3"), firstUserSessionId);

        List<SectionResponse> sections = sectionService.getAllSections(firstUserSessionId);

        assertAll(
                () -> assertNotNull(sections),
                () -> assertEquals(3, sections.size()),
                () -> assertEquals("TestSection1", sections.get(0).getSectionName()),
                () -> assertEquals("TestSection2", sections.get(1).getSectionName()),
                () -> assertEquals("TestSection3", sections.get(2).getSectionName())
        );
    }

    @Test
    public void testDeleteSection_superStatus() throws NoteServerException {
        firstUserSessionId = debugService.loginUser(firstRegisteredUser.getId());
        secondUserSessionId = debugService.loginUser(secondRegisteredUser.getId());
        debugService.makeAdmin(secondRegisteredUser);
        SectionResponse section = sectionService.createSection(new SectionRequest("TestSection"), firstUserSessionId);

        sectionService.deleteSection(secondUserSessionId, section.getId());

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.getSectionInfo(secondUserSessionId, section.getId());
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such section on the server"))
        );
    }
}
