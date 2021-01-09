package net.thumbtack.school.notes.dao;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionDaoTest {
    Section testSection;

    @Autowired
    ServerDao serverDao;

    @Autowired
    SectionDao sectionDao;

    @BeforeEach
    public void setUp() {
        serverDao.clear();
        testSection = new Section();
        testSection.setId(1);
        testSection.setSectionName("TestSection");
        testSection.setCreationTime(LocalDateTime.now());
    }

    @Test
    public void testCreateSection_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        int userId = user.getId();
        serverDao.logInUser(userId);
        int createdSectionId = sectionDao.createSection(testSection, userId);
        Section createdSection = sectionDao.getSectionInfo(createdSectionId);
        assertAll(
                () -> assertEquals(testSection.getSectionName(), createdSection.getSectionName()),
                () -> assertEquals(userId, createdSection.getAuthor().getId())
        );
    }

    @Test
    public void testRenameSection_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        int userId = user.getId();
        serverDao.logInUser(userId);
        int createdSectionId = sectionDao.createSection(testSection, userId);
        Section createdSection = sectionDao.getSectionInfo(createdSectionId);
        int renamedSectionId = sectionDao.renameSection(createdSection.getId(), "TestSectionNewName");
        Section renamedSection = sectionDao.getSectionInfo(renamedSectionId);

        assertAll(
                () -> assertEquals("TestSectionNewName", renamedSection.getSectionName()),
                () -> assertEquals(userId, renamedSection.getAuthor().getId())
        );
    }

    @Test
    public void testDeleteSection_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        int userId = user.getId();
        serverDao.logInUser(userId);
        int createdSectionId = sectionDao.createSection(testSection, userId);
        Section createdSection = sectionDao.getSectionInfo(createdSectionId);
        sectionDao.deleteSection(createdSection.getId());

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionDao.getSectionInfo(createdSection.getId());

        });
        assertAll(
                () -> assertNotNull(exception.getExceptionErrorInfo()),
                () -> assertTrue(exception.getExceptionErrorInfo().getErrorString()
                        .contains("No such section on the server"))
        );

    }

    @Test
    public void testGetSectionInfo_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        int userId = user.getId();
        serverDao.logInUser(userId);
        int createdSectionId = sectionDao.createSection(testSection, userId);
        Section createdSection = sectionDao.getSectionInfo(createdSectionId);

        assertAll(
                () -> assertEquals(testSection.getSectionName(), createdSection.getSectionName()),
                () -> assertEquals(userId, createdSection.getAuthor().getId())
        );
    }

    @Test
    public void testGetAllSectionsInfo_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        int userId = user.getId();
        serverDao.logInUser(userId);
        testSection.setSectionName("TestSection1");
        sectionDao.createSection(testSection, userId);
        testSection.setSectionName("TestSection2");
        sectionDao.createSection(testSection, userId);
        testSection.setSectionName("TestSection3");
        sectionDao.createSection(testSection, userId);

        List<Section> resultSections = sectionDao.getAllSections();

        assertAll(
                () -> assertEquals(3, resultSections.size()),
                () -> assertEquals(userId, resultSections.get(0).getAuthor().getId()),
                () -> assertEquals("TestSection1", resultSections.get(0).getSectionName()),
                () -> assertEquals(userId, resultSections.get(1).getAuthor().getId()),
                () -> assertEquals("TestSection2", resultSections.get(1).getSectionName()),
                () -> assertEquals(userId, resultSections.get(2).getAuthor().getId()),
                () -> assertEquals("TestSection3", resultSections.get(2).getSectionName())
        );
    }
}
