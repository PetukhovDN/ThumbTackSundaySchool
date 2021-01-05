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
        serverDao.logInUser(user.getId());
        testSection.setAuthorId(user.getId());
        sectionDao.createSection(testSection);
        Section resultSection = sectionDao.getSectionInfo(testSection.getId());

        assertAll(
                () -> assertEquals(testSection.getSectionName(), resultSection.getSectionName()),
                () -> assertEquals(testSection.getId(), resultSection.getId())
        );
    }

    @Test
    public void testRenameSection_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        serverDao.logInUser(user.getId());
        testSection.setAuthorId(user.getId());
        sectionDao.createSection(testSection);

        Section resultSection = sectionDao.renameSection(testSection.getId(), "TestSectionNewName");

        assertAll(
                () -> assertEquals("TestSectionNewName", resultSection.getSectionName()),
                () -> assertEquals(testSection.getId(), resultSection.getId())
        );
    }

    @Test
    public void testDeleteSection_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        serverDao.logInUser(user.getId());
        testSection.setAuthorId(user.getId());
        sectionDao.createSection(testSection);
        sectionDao.deleteSection(testSection.getId());

        NoteServerException exception = assertThrows(NoteServerException.class, () -> {
            sectionDao.getSectionInfo(testSection.getId());

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
        serverDao.logInUser(user.getId());
        testSection.setAuthorId(user.getId());
        sectionDao.createSection(testSection);

        Section resultSection = sectionDao.getSectionInfo(testSection.getId());

        assertAll(
                () -> assertEquals(testSection.getSectionName(), resultSection.getSectionName()),
                () -> assertEquals(testSection.getId(), resultSection.getId()),
                () -> assertEquals(testSection.getAuthorId(), resultSection.getAuthorId())
        );
    }

    @Test
    public void testGetAllSectionsInfo_rightParameters() throws NoteServerException {
        User user = serverDao.registerUser();
        serverDao.logInUser(user.getId());
        testSection.setAuthorId(user.getId());
        testSection.setSectionName("TestSection1");
        sectionDao.createSection(testSection);
        testSection.setSectionName("TestSection2");
        sectionDao.createSection(testSection);
        testSection.setSectionName("TestSection3");
        sectionDao.createSection(testSection);

        List<Section> resultSections = sectionDao.getAllSections();

        assertAll(
                () -> assertEquals(3, resultSections.size()),
                () -> assertEquals(testSection.getAuthorId(), resultSections.get(0).getAuthorId()),
                () -> assertEquals("TestSection1", resultSections.get(0).getSectionName()),
                () -> assertEquals(testSection.getAuthorId(), resultSections.get(1).getAuthorId()),
                () -> assertEquals("TestSection2", resultSections.get(1).getSectionName()),
                () -> assertEquals(testSection.getAuthorId(), resultSections.get(2).getAuthorId()),
                () -> assertEquals("TestSection3", resultSections.get(2).getSectionName())
        );
    }


}
