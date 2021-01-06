package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.NoteDao;
import net.thumbtack.school.notes.mappers.NoteMapper;
import net.thumbtack.school.notes.model.Note;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class NoteDaoImpl implements NoteDao {
    NoteMapper noteMapper;

    @Override
    public Note createNote(Note note, int sectionId) {
        return null;
    }

    @Override
    public Note getNoteInfo(int noteId) {
        return null;
    }

    @Override
    public Note changeNote(int noteId, String noteBody) {
        return null;
    }

    @Override
    public Note replaceNote(int noteId, int sectionId) {
        return null;
    }

    @Override
    public void deleteNote(int noteId) {

    }

    @Override
    public void deleteAllCommentsForNote(int noteId) {

    }

    @Override
    public void rateNote(int noteId, int rating) {

    }
}
