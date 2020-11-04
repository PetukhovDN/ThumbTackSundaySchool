package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.model.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionDaoImpl extends DaoImplBase implements SectionDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SectionDaoImpl.class);

    @Override
    public Section createSection(String token, Section section) {
        return null;
    }

    @Override
    public Section renameSection(String token, int sectionId, String newSectionName) {
        return null;
    }

    @Override
    public void deleteSection(String token, int sectionId) {
    }

    @Override
    public Section getSectionInfo(String token, int sectionId) {
        return null;
    }

    @Override
    public List<Section> getAllSections(String token) {
        return null;
    }
}
