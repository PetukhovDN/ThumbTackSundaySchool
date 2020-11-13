package net.thumbtack.school.notes.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.NoteDao;
import net.thumbtack.school.notes.mappers.NoteMapper;
import net.thumbtack.school.notes.model.Note;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NoteDaoImpl implements NoteDao {
    private final NoteMapper noteMapper;

    @Override
    public Note createNote(String token, Note note, int sectionId) {
        return null;
    }

    @Override
    public Note getNoteInfo(String token, int noteId) {
        return null;
    }

    @Override
    public Note changeNote(String token, int noteId, String noteBody) {
        return null;
    }

    @Override
    public Note replaceNote(String token, int noteId, int sectionId) {
        return null;
    }

    @Override
    public void deleteNote(String token, int noteId) {

    }

    @Override
    public void deleteAllCommentsForNote(String token, int noteId) {

    }

    @Override
    public void rateNote(String token, int noteId, int rating) {

    }
}
