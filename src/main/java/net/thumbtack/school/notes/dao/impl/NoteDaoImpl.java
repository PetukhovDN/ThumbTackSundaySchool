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

import java.util.List;

/**
 * DataAccessObject to work with notes
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class NoteDaoImpl implements NoteDao {
    NoteMapper noteMapper;

    /**
     * Method to save new note to the database
     * Note contains such information:
     * note subject, author identifier, section identifier
     *
     * @param note contains new note information
     * @return created note identifier in success
     * @throws NoteServerException if note with such identifier already exists in database
     */
    @Override
    public Integer createNote(Note note) throws NoteServerException {
        log.info("DAO insert Note {} to Database", note);
        try {
            noteMapper.saveNote(note);
            return note.getId();
        } catch (DuplicateKeyException ex) {
            log.error("Note with subject {} already exists", note.getSubject(), ex);
            throw new NoteServerException(ExceptionErrorInfo.NOTE_ALREADY_EXISTS, note.getSubject());
        } catch (RuntimeException ex) {
            log.error("Can't insert Note {} to Database, {}", note, ex);
            throw ex;
        }
    }

    /**
     * Method to save new note revision to the database
     *
     * @param noteRevision contains note body, revision identifier, note identifier
     * @return note revision identifier in success
     */
    @Override
    public String createNoteRevision(NoteRevision noteRevision) {
        log.info("DAO insert NoteRevision {} to Database", noteRevision);
        try {
            noteMapper.saveNoteRevision(noteRevision);
            return noteRevision.getRevisionId();
        } catch (RuntimeException ex) {
            log.error("Can't insert Note revision {} to Database, {}", noteRevision, ex);
            throw ex;
        }
    }

    /**
     * Method to get note information from database
     *
     * @param noteId note identifier
     * @return note information in success
     * @throws NoteServerException if there is no note with given identifier in database
     */
    @Override
    public Note getNoteInfo(int noteId) throws NoteServerException {
        log.info("DAO get information about Note with id {} from Database", noteId);
        try {
            Note note = noteMapper.getNoteById(noteId);
            if (note == null) {
                log.error("No such note on the server");
                throw new NoteServerException(ExceptionErrorInfo.NOTE_DOES_NOT_EXISTS, String.valueOf(noteId));
            }
            return note;
        } catch (RuntimeException ex) {
            log.error("Can't get information about Note with id {} from Database, {}", noteId, ex);
            throw ex;
        }
    }

    /**
     * Method to get note revision from database
     *
     * @param revisionId revision identifier
     * @param noteId     note identifier
     * @return note revision information in success
     * @throws NoteServerException if there is no note revision with given identifier in database
     */
    @Override
    public NoteRevision getNoteRevisionInfo(String revisionId, int noteId) throws NoteServerException {
        log.info("DAO get Note with id {} body information from Database", noteId);
        try {
            NoteRevision noteRevision = noteMapper.getNoteRevision(revisionId);
            if (noteRevision == null) {
                log.error("No such note on the server");
                throw new NoteServerException(ExceptionErrorInfo.NOTE_DOES_NOT_EXISTS, String.valueOf(noteId));
            }
            return noteRevision;
        } catch (RuntimeException ex) {
            log.error("Can't get information about body of the Note with id {} from Database, {}", noteId, ex);
            throw ex;
        }
    }

    /**
     * Method to add to created note last revision identifier
     *
     * @param noteId     note identifier
     * @param revisionId revision identifier
     */
    @Override
    public void updateNoteLastRevision(int noteId, String revisionId) {
        log.info("DAO save Note with id {} last revision id to Database", noteId);
        try {
            noteMapper.updateNoteLastRevisionId(noteId, revisionId);
        } catch (RuntimeException ex) {
            log.error("Can't save last revision id of the Note with id {} to Database, {}", noteId, ex);
            throw ex;
        }
    }

    /**
     * Method to change section identifier for the existing note
     *
     * @param noteId    note identifier
     * @param sectionId section identifier
     */
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

    /**
     * Method to delete note with given identifier from database
     *
     * @param noteId identifier of the note to delete
     */
    @Override
    public void deleteNote(int noteId) {
        log.info("DAO delete Note with id {} from Database", noteId);
        try {
            noteMapper.deleteNoteById(noteId);
        } catch (RuntimeException ex) {
            log.error("Can't delete Note with id {} from Database, {}", noteId, ex);
            throw ex;
        }
    }

    /**
     * Method to add rating to the existing note
     *
     * @param noteId note identifier
     * @param userId identifier of user that rates note
     * @param rating new rating for the note
     */
    @Override
    public void rateNote(int noteId, int userId, int rating) {
        log.info("DAO add rating to Note with id {} to Database", noteId);
        try {
            noteMapper.addRatingForNote(noteId, userId, rating);
        } catch (RuntimeException ex) {
            log.error("Can't add rating to Note with id {} to Database, {}", noteId, ex);
            throw ex;
        }
    }

    /**
     * Method to get all notes from the database
     *
     * @return list, which contains all notes information
     */
    public List<Note> getAllNotes() {
        log.info("DAO get all notes from Database");
        try {
            return noteMapper.getAllNotes();
        } catch (RuntimeException ex) {
            log.error("Can't get all notes from Database", ex);
            throw ex;
        }
    }
}
