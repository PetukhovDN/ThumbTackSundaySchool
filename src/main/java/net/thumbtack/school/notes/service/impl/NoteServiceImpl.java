package net.thumbtack.school.notes.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.NoteDaoImpl;
import net.thumbtack.school.notes.dao.impl.SessionDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.dto.mappers.NoteMapStruct;
import net.thumbtack.school.notes.dto.request.note.EditRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class NoteServiceImpl implements NoteService {
    NoteDaoImpl noteDao;
    SessionDaoImpl sessionDao;
    UserDaoImpl userDao;

    @Override
    @Transactional
    public NoteResponse createNote(NoteRequest createRequest, String sessionId) throws NoteServerException {
        log.info("Trying to create new note");
        int currentUserId = sessionDao.getSessionBySessionId(sessionId).getUserId();
        User currentUser = userDao.getUserById(currentUserId);
        Note note = NoteMapStruct.INSTANCE.requestCreateNote(createRequest);
        note.setAuthor(currentUser);

        Note createdNote = noteDao.createNote(note);

        NoteRevision noteRevision = NoteMapStruct.INSTANCE.requestCreateNoteRevision(createRequest);
        noteRevision.setNoteId(createdNote.getId());
        String revisionId = UUID.randomUUID().toString();
        noteRevision.setRevisionId(revisionId);

        NoteRevision createdNoteRevision = noteDao.createNoteRevision(noteRevision);

        Note updatedNote = noteDao.updateNoteLastRevision(createdNote.getId(), revisionId);

        return NoteMapStruct.INSTANCE.responseCreateNote(updatedNote, createdNoteRevision);
    }

    @Override
    @Transactional
    public NoteResponse getNoteInfo(int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to get information about note with id {} ", noteId);
        sessionDao.getSessionBySessionId(sessionId);

        Note note = noteDao.getNoteInfo(noteId);
        NoteRevision noteRevision = noteDao.getNoteRevisionInfo(note.getLastRevisionId(), noteId);
        return NoteMapStruct.INSTANCE.responseCreateNote(note, noteRevision);
    }

    @Override
    @Transactional
    public NoteResponse editNote(EditRequest editRequest, int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to edit information about note with id {} ", noteId);
        int currentUserId = sessionDao.getSessionBySessionId(sessionId).getUserId();
        User user = userDao.getUserById(currentUserId);
        if (!user.getUserStatus().equals(UserStatus.ADMIN)) {
            int authorId = noteDao.getNoteInfo(noteId).getAuthor().getId();
            if (authorId != currentUserId) {
                throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_SECTION, "You are not creator of this section");
            }
        }

        String body = editRequest.getBody();
        String sectionIdString = editRequest.getSectionId();
        int sectionId;

        String revisionId = UUID.randomUUID().toString();
        NoteRevision noteRevision = new NoteRevision();
        noteRevision.setNoteId(noteId);
        noteRevision.setRevisionId(revisionId);
        noteRevision.setBody(body);
        try {
            sectionId = Integer.parseInt(sectionIdString);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_SECTION_IDENTIFIER, "Incorrect section id");
        }
        if (body.isEmpty() && sectionIdString.isBlank()) {
            throw new NoteServerException(ExceptionErrorInfo.EDIT_PARAMETERS_REQUIRED, "At least one parameter is required");
        } else if (body.isBlank()) {
            noteDao.replaceNoteToOtherSection(noteId, sectionId);
        } else if (sectionIdString.isBlank()) {
            noteDao.createNoteRevision(noteRevision);
            noteDao.updateNoteLastRevision(noteId, revisionId);
        } else {
            noteDao.createNoteRevision(noteRevision);
            noteDao.updateNoteLastRevision(noteId, revisionId);
            noteDao.replaceNoteToOtherSection(noteId, sectionId);
        }
        Note resultNote = noteDao.getNoteInfo(noteId);
        NoteRevision resultNoteRevision = noteDao.getNoteRevisionInfo(resultNote.getLastRevisionId(), resultNote.getId());
        return NoteMapStruct.INSTANCE.responseCreateNote(resultNote, resultNoteRevision);
    }

    @Override
    @Transactional
    public void deleteNote(int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to delete note");
        int currentUserId = sessionDao.getSessionBySessionId(sessionId).getUserId();
        User user = userDao.getUserById(currentUserId);
        if (!user.getUserStatus().equals(UserStatus.ADMIN)) {
            int authorId = noteDao.getNoteInfo(noteId).getAuthor().getId();
            if (authorId != currentUserId) {
                throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_SECTION, "You are not creator of this section");
            }
        }
        noteDao.deleteNote(noteId);
    }
}
