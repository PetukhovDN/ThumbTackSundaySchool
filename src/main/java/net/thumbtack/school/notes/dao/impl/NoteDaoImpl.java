package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.NoteDao;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.NoteMapper;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class NoteDaoImpl implements NoteDao {
    NoteMapper noteMapper;

    @Override
    public Note createNote(Note note) throws NoteServerException {
        log.info("DAO insert Note {} to Database", note);
        try {
            noteMapper.saveNote(note);
            return noteMapper.getNoteBySubject(note.getSubject());
        } catch (DuplicateKeyException ex) {
            log.error("Note with subject {} already exists", note.getSubject(), ex);
            throw new NoteServerException(ExceptionErrorInfo.NOTE_ALREADY_EXISTS, note.getSubject());
        } catch (RuntimeException ex) {
            log.error("Can't insert Note {} to Database, {}", note, ex);
            throw ex;
        }
    }

    @Override
    public NoteRevision createNoteRevision(NoteRevision noteRevision) {
        log.info("DAO insert NoteRevision {} to Database", noteRevision);
        try {
            noteMapper.saveNoteRevision(noteRevision);
            return noteMapper.getNoteRevision(noteRevision.getRevisionId(), noteRevision.getNoteId());
        } catch (RuntimeException ex) {
            log.error("Can't insert Note revision {} to Database, {}", noteRevision, ex);
            throw ex;
        }
    }

    @Override
    public Note getNoteInfo(int noteId) throws NoteServerException {
        log.info("DAO get information about Note with id {} from Database", noteId);
        try {
            Note note = noteMapper.getNoteById(noteId);
            if (note == null) {
                log.error("No such note on the server");
                throw new NoteServerException(ExceptionErrorInfo.NOTE_DOES_NOT_EXISTS, "No such note on the server");
            }
            return note;
        } catch (RuntimeException ex) {
            log.error("Can't get information about Note with id {} from Database, {}", noteId, ex);
            throw ex;
        }
    }

    @Override
    public NoteRevision getNoteRevisionInfo(String revisionId, int noteId) throws NoteServerException {
        log.info("DAO get Note with id {} body information from Database", noteId);
        try {
            NoteRevision noteRevision = noteMapper.getNoteRevision(revisionId, noteId);
            if (noteRevision == null) {
                log.error("No such note on the server");
                throw new NoteServerException(ExceptionErrorInfo.NOTE_DOES_NOT_EXISTS, "No such note on the server");
            }
            return noteRevision;
        } catch (RuntimeException ex) {
            log.error("Can't get information about body of the Note with id {} from Database, {}", noteId, ex);
            throw ex;
        }
    }

    @Override
    public Note updateNoteLastRevision(int noteId, String revisionId) {
        log.info("DAO save Note with id {} last revision id to Database", noteId);
        try {
            noteMapper.updateNoteLastRevisionId(noteId, revisionId);
            return noteMapper.getNoteById(noteId);
        } catch (RuntimeException ex) {
            log.error("Can't save last revision id of the Note with id {} to Database, {}", noteId, ex);
            throw ex;
        }
    }

    @Override
    public void replaceNoteToOtherSection(int noteId, int sectionId) {
        log.info("DAO change section id to the {} for Note with id {} in Database", sectionId, noteId);
        try {
            noteMapper.changeSectionId(noteId, sectionId);
        } catch (RuntimeException ex) {
            log.error("Can't change section id of the Note with id {} in Database, {}", noteId, ex);
            throw ex;
        }
    }

    @Override
    public void deleteNote(int noteId) {
        log.info("DAO delete Note with id {} from Database", noteId);
        try {
            noteMapper.deleteNoteById(noteId);
        } catch (RuntimeException ex) {
            log.error("Can't delete note with id {} from Database, {}", noteId, ex);
            throw ex;
        }
    }

    @Override
    public void deleteAllCommentsForNote(int noteId) {

    }

    @Override
    public void rateNote(int noteId, int rating) {

    }
}
