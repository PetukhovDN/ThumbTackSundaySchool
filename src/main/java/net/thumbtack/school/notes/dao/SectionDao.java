package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.model.Section;

import java.util.List;

public interface SectionDao {
    Section createSection(String token, Section section);

    Section renameSection(String token, int sectionId, String newSectionName);

    void deleteSection(String token, int sectionId);

    Section getSectionInfo(String token, int sectionId);

    List<Section> getAllSections(String token);
}
