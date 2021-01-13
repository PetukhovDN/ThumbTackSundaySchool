package net.thumbtack.school.notes.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.*;
import net.thumbtack.school.notes.dto.mappers.NoteMapStruct;
import net.thumbtack.school.notes.dto.request.comment.RateNoteRequest;
import net.thumbtack.school.notes.dto.request.note.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.dto.response.note.NotesInfoResponseWithParams;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.params.NoteRequestParam;
import net.thumbtack.school.notes.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to work with notes on the server
 * In every method check`s if session is alive and updates session life time after successful request
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class NoteServiceImpl implements NoteService {
    NoteDaoImpl noteDao;
    SessionDaoImpl sessionDao;
    UserDaoImpl userDao;
    CommentDaoImpl commentDao;
    SectionDaoImpl sectionDao;

    /**
     * Method to create note ont the server
     * Note has it`s own author and is located in the sections, which is specified in creating request
     *
     * @param createRequest contains subject of the note, body of the note and section in which this note will be located
     * @param sessionId     session token of current user
     * @return information about created note in success
     */
    @Override
    @Transactional
    public NoteResponse createNote(NoteRequest createRequest, String sessionId) throws NoteServerException {
        log.info("Trying to create new note");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        Note note = NoteMapStruct.INSTANCE.requestCreateNote(createRequest);
        int currentUserId = userSession.getUserId();
        User currentUser = userDao.getUserById(currentUserId);
        note.setAuthor(currentUser);
        int sectionId = createRequest.getSectionId();
        try {
            Section section = sectionDao.getSectionInfo(sectionId);
            note.setSection(section);
        } catch (NullPointerException exception) {
            throw new NoteServerException(ExceptionErrorInfo.SECTION_DOES_NOT_EXISTS, String.valueOf(sectionId));
        }
        int createdNoteId = noteDao.createNote(note);
        String revisionId = UUID.randomUUID().toString();
        NoteRevision noteRevision = NoteMapStruct.INSTANCE.requestCreateNoteRevision(createRequest);
        noteRevision.setNoteId(createdNoteId);
        noteRevision.setRevisionId(revisionId);
        String createdNoteRevisionId = noteDao.createNoteRevision(noteRevision);
        NoteRevision createdNoteRevision = noteDao.getNoteRevisionInfo(createdNoteRevisionId, createdNoteId);
        noteDao.updateNoteLastRevision(createdNoteId, revisionId);
        Note updatedNote = noteDao.getNoteInfo(createdNoteId);
        NoteResponse response = NoteMapStruct.INSTANCE.responseCreateNote(updatedNote, createdNoteRevision);
        sessionDao.updateSession(userSession);
        return response;
    }

    /**
     * Method to get information about note with given identifier
     *
     * @param noteId    identifier of note to get information
     * @param sessionId session token of current user
     * @return information about needed note in success
     */
    @Override
    @Transactional
    public NoteResponse getNoteInfo(int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to get information about note with id {} ", noteId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        Note note = noteDao.getNoteInfo(noteId);
        NoteRevision noteRevision = noteDao.getNoteRevisionInfo(note.getLastRevisionId(), noteId);
        NoteResponse response = NoteMapStruct.INSTANCE.responseCreateNote(note, noteRevision);
        sessionDao.updateSession(userSession);
        return response;
    }

    /**
     * Method to edit note information or to replace it from one section to another
     * If contains only note body - creates new note body, old is saved in previous revision
     * If contains only note section - replaces note to the new section
     * If both - creates new note body and replaces note to the new section
     *
     * @param editNoteRequest contains new note body or new note section identifier, or both
     * @param noteId          identifier of given note
     * @param sessionId       session token of current user
     * @return information about changed note in success
     * @throws NoteServerException if current user is not the author of note to edit
     */
    @Override
    @Transactional
    public NoteResponse editNote(EditNoteRequest editNoteRequest, int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to edit information about note with id {} ", noteId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        User user = userDao.getUserById(currentUserId);
        if (!user.getUserStatus().equals(UserStatus.ADMIN)) {
            User author = noteDao.getNoteInfo(noteId).getAuthor();
            int authorId = author.getId();
            if (currentUserId != authorId) {
                throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_NOTE, "Author is " + author.getLogin());
            }
        }
        String body = editNoteRequest.getBody();
        String sectionIdString = editNoteRequest.getSectionId();
        String revisionId = UUID.randomUUID().toString();
        NoteRevision noteRevision = new NoteRevision();
        noteRevision.setNoteId(noteId);
        noteRevision.setRevisionId(revisionId);
        noteRevision.setBody(body);
        int sectionId;

        if (body == null && sectionIdString == null) {
            throw new NoteServerException(ExceptionErrorInfo.EDIT_PARAMETERS_REQUIRED, body + " | " + sectionIdString);
        } else if (sectionIdString == null) {
            noteDao.createNoteRevision(noteRevision);
            noteDao.updateNoteLastRevision(noteId, revisionId);
        } else if (body == null) {
            try {
                sectionId = Integer.parseInt(sectionIdString);
            } catch (NumberFormatException ex) {
                throw new NoteServerException(ExceptionErrorInfo.INCORRECT_SECTION_IDENTIFIER, sectionIdString);
            }
            sectionDao.getSectionInfo(sectionId);
            noteDao.replaceNoteToOtherSection(noteId, sectionId);
        } else {
            try {
                sectionId = Integer.parseInt(sectionIdString);
            } catch (NumberFormatException ex) {
                throw new NoteServerException(ExceptionErrorInfo.INCORRECT_SECTION_IDENTIFIER, sectionIdString);
            }
            sectionDao.getSectionInfo(sectionId);
            noteDao.createNoteRevision(noteRevision);
            noteDao.updateNoteLastRevision(noteId, revisionId);
            noteDao.replaceNoteToOtherSection(noteId, sectionId);
        }
        Note resultNote = noteDao.getNoteInfo(noteId);
        NoteRevision resultNoteRevision = noteDao.getNoteRevisionInfo(resultNote.getLastRevisionId(), resultNote.getId());
        NoteResponse response = NoteMapStruct.INSTANCE.responseCreateNote(resultNote, resultNoteRevision);
        sessionDao.updateSession(userSession);
        return response;
    }

    /**
     * Method to delete note with given identifier and all it`s revisions
     * Only author of the note (or server admin) can delete it
     *
     * @param noteId    identifier of note to delete
     * @param sessionId session token of current user
     * @throws NoteServerException if current user is not the author if note
     */
    @Override
    @Transactional
    public void deleteNote(int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to delete note");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        User user = userDao.getUserById(currentUserId);
        if (!user.getUserStatus().equals(UserStatus.ADMIN)) {
            User author = noteDao.getNoteInfo(noteId).getAuthor();
            int authorId = author.getId();
            if (currentUserId != authorId) {
                throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_NOTE, "Author is " + author.getLogin());
            }
        }
        noteDao.deleteNote(noteId);
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to delete all note comments
     * Only author of the note can delete all its comments
     *
     * @param noteId    identifier of the note, which comments need to delete
     * @param sessionId session token of current user
     * @throws NoteServerException if current user is not the author if note
     */
    @Override
    @Transactional
    public void deleteAllNoteComments(int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to delete all comments for note with id {} ", noteId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        User author = noteDao.getNoteInfo(noteId).getAuthor();
        int authorId = author.getId();
        if (currentUserId != authorId) {
            throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_NOTE, "Author is " + author.getLogin());
        }
        commentDao.deleteAllNoteComments(noteId);
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to add rating to the note
     *
     * @param rateRequest contains new rating (from 1 to 5)
     * @param noteId      identifier of the note to rate
     * @param sessionId   session token of the current user
     * @throws NoteServerException if current user is not the author if note
     */
    @Override
    @Transactional
    public void rateNote(RateNoteRequest rateRequest, int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to rate note with id {} ", noteId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        User author = noteDao.getNoteInfo(noteId).getAuthor();
        int authorId = author.getId();
        if (currentUserId == authorId) {
            throw new NoteServerException(ExceptionErrorInfo.CANNOT_RATE_YOUR_OWN_NOTE, "Author: " + author.getLogin());
        }
        noteDao.rateNote(noteId, currentUserId, Integer.parseInt(rateRequest.getRating()));
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to get notes from the server with parameters:
     *
     * @param requestParam contains parameters, in which user wants to get user notes from the server
     * @param sessionId    session token of the current user
     * @return list of notes, according to the parameters, in success
     */
    //TODO: logic for parameters, now returns all notes from the server
    @Override
    @Transactional
    public List<NotesInfoResponseWithParams> getNotesInfo(NoteRequestParam requestParam, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to get all notes info");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        List<Note> notes = noteDao.getAllNotes();
        ArrayList<NotesInfoResponseWithParams> notesResponses = new ArrayList<>();
        for (Note note : notes) {
            NoteRevision noteRevision = noteDao.getNoteRevisionInfo(note.getLastRevisionId(), note.getId());
            NotesInfoResponseWithParams response = NoteMapStruct.INSTANCE.responseGetAllNotes(note, noteRevision);
            notesResponses.add(response);
        }
        sessionDao.updateSession(userSession);
        return notesResponses;
    }
}
