package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.SectionMapper;
import net.thumbtack.school.notes.model.Section;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataAccessObject to work with note`s sections
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class SectionDaoImpl implements SectionDao {
    SectionMapper sectionMapper;

    /**
     * Method to save section to the database
     *
     * @param section contains new section information
     * @return section information in success
     * @throws NoteServerException if section with such name already exists in database
     */
    @Override
    public Section createSection(Section section) throws NoteServerException {
        log.info("DAO insert Section {} to Database", section);
        try {
            sectionMapper.saveSection(section);
            return sectionMapper.getSectionByName(section.getSectionName());
        } catch (DuplicateKeyException ex) {
            log.error("Section {} already exists", section.getSectionName(), ex);
            throw new NoteServerException(ExceptionErrorInfo.SECTION_ALREADY_EXISTS, section.getSectionName());
        } catch (RuntimeException ex) {
            log.error("Can't insert Section {} to Database, {}", section, ex);
            throw ex;
        }
    }

    /**
     * Method to change section name in database
     *
     * @param sectionId      identifier of section to be changed
     * @param newSectionName new section name
     * @return section information in success
     */
    @Override
    public Section renameSection(int sectionId, String newSectionName) {
        log.info("DAO change Section name {} in Database", newSectionName);
        try {
            sectionMapper.updateSection(sectionId, newSectionName);
            return sectionMapper.getSectionByName(newSectionName);
        } catch (RuntimeException ex) {
            log.error("Can't rename Section with name {} in Database, {}", newSectionName, ex);
            throw ex;
        }
    }

    /**
     * Method to delete section information from database
     *
     * @param sectionId identifier of section to be deleted
     */
    @Override
    public void deleteSection(int sectionId) {
        log.info("DAO delete Section with id {} from Database", sectionId);
        try {
            sectionMapper.deleteSection(sectionId);
        } catch (RuntimeException ex) {
            log.error("Can't delete Section with id {} from Database, {}", sectionId, ex);
            throw ex;
        }
    }

    /**
     * Method to get section information
     *
     * @param sectionId identifier of section for which information is needed
     * @return section information in success
     * @throws NoteServerException if there is no section with given identifier in database
     */
    @Override
    public Section getSectionInfo(int sectionId) throws NoteServerException {
        log.info("DAO get information about Section with id {} from Database", sectionId);
        try {
            Section section = sectionMapper.getSectionById(sectionId);
            if (section == null) {
                log.error("No such section on the server");
                throw new NoteServerException(ExceptionErrorInfo.SECTION_DOES_NOT_EXISTS, "No such section on the server");
            }
            return section;
        } catch (RuntimeException ex) {
            log.error("Can't get information about Section with id {} from Database, {}", sectionId, ex);
            throw ex;
        }
    }

    /**
     * Method to get information about all sections, saved in database
     *
     * @return list of existing in database sections
     */
    @Override
    public List<Section> getAllSections() {
        log.info("DAO get information about all Sections from Database");
        try {
            return sectionMapper.getAllSections();
        } catch (RuntimeException ex) {
            log.error("Can't get information about all Sections from Database, ", ex);
            throw ex;
        }
    }
}
