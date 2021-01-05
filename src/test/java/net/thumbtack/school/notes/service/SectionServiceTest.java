package net.thumbtack.school.notes.service;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;
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
    User registeredUser;
    String testSessionId;

    @Autowired
    DebugService debugService;

    @Autowired
    SectionService sectionService;

    @BeforeEach
    public void setUp() {
        debugService.clearDatabase();
        registeredUser = debugService.registerUser();
        testSessionId = debugService.loginUser(registeredUser.getId());
    }

    @Test
    public void testCreateSection_wrongSessionID() {
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
        SectionRequest createRequest = new SectionRequest("TestSection");
        Section section = sectionService.createSection(createRequest, testSessionId);

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
        SectionRequest createRequest = new SectionRequest("TestSection");
        Section section = sectionService.createSection(createRequest, testSessionId);
        User anotherUser = debugService.registerUser();
        String anotherUserSessionId = debugService.loginUser(anotherUser.getId());

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.deleteSection(anotherUserSessionId, section.getId());
        });

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("You are not creator of this section"))
        );
    }

    @Test
    public void testGetSectionInfo_notExistingSection() {
        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.getSectionInfo(testSessionId, 77);
        });
        System.out.println(exception.getExceptionErrorInfo().toString());

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such section on the server"))
        );
    }

    @Test
    public void testGetAllSectionsInfo_wrongSessionId() throws NoteServerException {
        sectionService.createSection(new SectionRequest("TestSection1"), testSessionId);
        sectionService.createSection(new SectionRequest("TestSection2"), testSessionId);
        sectionService.createSection(new SectionRequest("TestSection3"), testSessionId);

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
        sectionService.createSection(new SectionRequest("TestSection1"), testSessionId);
        sectionService.createSection(new SectionRequest("TestSection2"), testSessionId);
        sectionService.createSection(new SectionRequest("TestSection3"), testSessionId);

        List<Section> sections = sectionService.getAllSections(testSessionId);

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
        Section section = sectionService.createSection(new SectionRequest("TestSection"), testSessionId);
        User secondUser = debugService.registerUser();
        User admin = debugService.makeAdmin(secondUser);
        String adminSessionId = debugService.loginUser(admin.getId());

        sectionService.deleteSection(adminSessionId, section.getId());

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionService.getSectionInfo(adminSessionId, section.getId());
        });

        System.out.println(exception.getExceptionErrorInfo().toString());

        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such section on the server"))
        );
    }


}
