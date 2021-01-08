package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct methods to transform user requests from note controller to the note models and models to the responses
 */
@Mapper
public interface NoteMapStruct {
    NoteMapStruct INSTANCE = Mappers.getMapper(NoteMapStruct.class);

    /**
     * Transforms request to create note to the note model
     */
    @Mappings({
            @Mapping(target = "subject", source = "subject"),
            @Mapping(target = "section.id", source = "sectionId")
    })
    Note requestCreateNote(NoteRequest createRequest);

    /**
     * Transforms request to create note body to the note revision model
     */
    @Mappings({
            @Mapping(target = "body", source = "body")
    })
    NoteRevision requestCreateNoteRevision(NoteRequest createRequest);

    /**
     * Transforms created note and note body models to the response
     */
    @Mappings({
            @Mapping(target = "id", source = "note.id"),
            @Mapping(target = "subject", source = "note.subject"),
            @Mapping(target = "body", source = "noteRevision.body"),
            @Mapping(target = "sectionId", source = "note.section.id"),
            @Mapping(target = "authorId", source = "note.author.id"),
            @Mapping(target = "created", source = "note.creationTime"),
            @Mapping(target = "revisionId", source = "note.lastRevisionId")
    })
    NoteResponse responseCreateNote(Note note, NoteRevision noteRevision);
}
