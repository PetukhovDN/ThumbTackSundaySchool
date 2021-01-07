package net.thumbtack.school.notes.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.mappers.SectionMupStruct;
import net.thumbtack.school.notes.dto.request.note.EditRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.service.impl.NoteServiceImpl;
import net.thumbtack.school.notes.service.impl.SectionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(value = "/api")
public class NoteController {
    final NoteServiceImpl noteService;
    final SectionServiceImpl sectionService;
    final String JAVASESSIONID = "JAVASESSIONID";

    public NoteController(NoteServiceImpl noteService, SectionServiceImpl sectionService) {
        this.noteService = noteService;
        this.sectionService = sectionService;
    }

    @PostMapping(value = "sections",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SectionResponse createSection(@RequestBody @Valid SectionRequest createRequest,
                                         @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return SectionMupStruct.INSTANCE.responseCreateSection(sectionService.createSection(createRequest, sessionId));
    }

    @PutMapping(value = "sections/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SectionResponse renameSection(@PathVariable(value = "id") int id,
                                         @RequestBody @Valid SectionRequest renameRequest,
                                         @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return SectionMupStruct.INSTANCE.responseCreateSection(sectionService.renameSection(renameRequest, sessionId, id));
    }

    @DeleteMapping(value = "sections/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSection(@PathVariable(value = "id") int id,
                              @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        sectionService.deleteSection(sessionId, id);
    }

    @GetMapping(value = "sections/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SectionResponse getSectionInfo(@PathVariable(value = "id") int id,
                                          @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return SectionMupStruct.INSTANCE.responseCreateSection(sectionService.getSectionInfo(sessionId, id));

    }

    @GetMapping(value = "sections",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<SectionResponse> getAllSectionsInfo(@CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        List<SectionResponse> responses = new ArrayList<>();
        List<Section> sections = sectionService.getAllSections(sessionId);
        for (Section section : sections) {
            responses.add(SectionMupStruct.INSTANCE.responseCreateSection(section));
        }
        return responses;
    }

    @PostMapping(value = "notes",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Valid
    public NoteResponse createNote(@RequestBody @Valid NoteRequest createRequest,
                                   @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return noteService.createNote(createRequest, sessionId);
    }

    @GetMapping(value = "notes/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse getNoteInfo(@PathVariable(value = "id") int noteId,
                                    @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return noteService.getNoteInfo(noteId, sessionId);
    }

    @PutMapping(value = "notes/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse editNote(@RequestBody @Valid EditRequest editRequest,
                                 @PathVariable(value = "id") int noteId,
                                 @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return noteService.editNote(editRequest, noteId, sessionId);
    }

    @DeleteMapping(value = "notes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNote(@PathVariable(value = "id") int noteId,
                           @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        noteService.deleteNote(noteId, sessionId);
    }
}
