package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.mappers.NoteMapStruct;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.dto.response.note.NotesInfoResponseWithParams;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoteMapStructTest {
    NoteMapStruct INSTANCE = Mappers.getMapper(NoteMapStruct.class);

    @Test
    void testTransformCreateNoteRequestToNoteAndNoteRevisionModels() {
        NoteRequest createRequest = new NoteRequest(
                "note_subject",
                "note_body",
                1
        );
        Note resultNote = INSTANCE.requestCreateNote(createRequest);
        Section section = new Section();
        section.setId(createRequest.getSectionId());
        resultNote.setSection(section);
        NoteRevision resultNoteRevision = INSTANCE.requestCreateNoteRevision(createRequest);

        assertAll(
                () -> assertEquals(createRequest.getSubject(), resultNote.getSubject()),
                () -> assertEquals(createRequest.getBody(), resultNoteRevision.getBody()),
                () -> assertEquals(createRequest.getSectionId(), resultNote.getSection().getId())
        );
    }

    @Test
    void testTransformNoteAndNoteRevisionModelsToNoteResponse() {
        Section section = new Section();
        section.setId(1);
        User user = new User();
        user.setId(2);

        Note note = new Note();
        note.setId(3);
        note.setSubject("note_subject");
        note.setSection(section);
        note.setAuthor(user);
        note.setCreationTime(LocalDateTime.now());
        note.setLastRevisionId(UUID.randomUUID().toString());

        NoteRevision noteRevision = new NoteRevision();
        noteRevision.setBody("note_body");

        NoteResponse noteResponse = INSTANCE.responseCreateNote(note, noteRevision);

        assertAll(
                () -> assertEquals(note.getId(), noteResponse.getId()),
                () -> assertEquals(note.getSubject(), noteResponse.getSubject()),
                () -> assertEquals(note.getSection().getId(), noteResponse.getSectionId()),
                () -> assertEquals(note.getAuthor().getId(), noteResponse.getAuthorId()),
                () -> assertEquals(note.getCreationTime(), noteResponse.getCreated()),
                () -> assertEquals(note.getLastRevisionId(), noteResponse.getRevisionId()),
                () -> assertEquals(noteResponse.getBody(), noteResponse.getBody())
        );
    }

    @Test
    void testTransformNoteAndNoteRevisionsModelsToNotesInfoResponseWithParams() {
        Section section = new Section();
        section.setId(1);
        User user = new User();
        user.setId(2);

        Note note = new Note();
        NoteRevision noteRevision = new NoteRevision();
        noteRevision.setBody("note_body");
        note.setId(3);
        note.setSubject("note_subject");
        note.setSection(section);
        note.setAuthor(user);
        note.setCreationTime(LocalDateTime.now());

        ArrayList<NoteRevision> revisions = new ArrayList<>();
        revisions.add(noteRevision);
        note.setRevisions(revisions);

        NotesInfoResponseWithParams noteResponse = INSTANCE.responseGetAllNotes(note, noteRevision);

        assertAll(
                () -> assertEquals(note.getId(), noteResponse.getId()),
                () -> assertEquals(note.getSubject(), noteResponse.getSubject()),
                () -> assertEquals(note.getSection().getId(), noteResponse.getSectionId()),
                () -> assertEquals(note.getAuthor().getId(), noteResponse.getAuthorId()),
                () -> assertEquals(note.getCreationTime(), noteResponse.getCreated()),
                () -> assertEquals(note.getRevisions(), noteResponse.getRevisions()),
                () -> assertEquals(noteResponse.getBody(), noteResponse.getBody())
        );
    }
}
