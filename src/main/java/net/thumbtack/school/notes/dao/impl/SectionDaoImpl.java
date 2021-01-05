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

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class SectionDaoImpl implements SectionDao {
    SectionMapper sectionMapper;

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

    @Override
    public Section renameSection(long sectionId, String newSectionName) {
        log.info("DAO change Section name {} in Database", newSectionName);
        try {
            sectionMapper.updateSection(sectionId, newSectionName);
            return sectionMapper.getSectionByName(newSectionName);
        } catch (RuntimeException ex) {
            log.error("Can't rename Section with name {} in Database, {}", newSectionName, ex);
            throw ex;
        }
    }

    @Override
    public void deleteSection(long sectionId) {
        log.info("DAO delete Section with id {} from Database", sectionId);
        try {
            sectionMapper.deleteSection(sectionId);
        } catch (RuntimeException ex) {
            log.error("Can't delete Section with id {} from Database, {}", sectionId, ex);
            throw ex;
        }
    }

    @Override
    public Section getSectionInfo(long sectionId) throws NoteServerException {
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
