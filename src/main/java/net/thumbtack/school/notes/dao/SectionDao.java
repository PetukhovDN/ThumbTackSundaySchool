package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;

import java.util.List;

public interface SectionDao {
    Section createSection(Section section) throws NoteServerException;

    Section renameSection(int sectionId, String newSectionName);

    void deleteSection(int sectionId);

    Section getSectionInfo(int sectionId) throws NoteServerException;

    List<Section> getAllSections();
}
