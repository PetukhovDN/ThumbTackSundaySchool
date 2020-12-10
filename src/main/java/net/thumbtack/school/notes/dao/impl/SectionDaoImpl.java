package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.mappers.SectionMapper;
import net.thumbtack.school.notes.model.Section;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class SectionDaoImpl implements SectionDao {
    SectionMapper sectionMapper;

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
