package net.thumbtack.school.notes.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.request.comment.CommentRequest;
import net.thumbtack.school.notes.dto.request.comment.EditCommentRequest;
import net.thumbtack.school.notes.dto.request.comment.RateNoteRequest;
import net.thumbtack.school.notes.dto.request.note.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentResponse;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.dto.response.note.NotesInfoResponseWithParams;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.enums.ParamInclude;
import net.thumbtack.school.notes.enums.ParamSort;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.params.NoteRequestParam;
import net.thumbtack.school.notes.service.impl.CommentServiceImpl;
import net.thumbtack.school.notes.service.impl.NoteServiceImpl;
import net.thumbtack.school.notes.service.impl.SectionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(value = "/api")
public class NoteController {
    final NoteServiceImpl noteService;
    final SectionServiceImpl sectionService;
    final CommentServiceImpl commentService;
    final String JAVASESSIONID = "JAVASESSIONID";

    public NoteController(NoteServiceImpl noteService,
                          SectionServiceImpl sectionService,
                          CommentServiceImpl commentService) {
        this.noteService = noteService;
        this.sectionService = sectionService;
        this.commentService = commentService;
    }

    @PostMapping(value = "sections",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SectionResponse createSection(@RequestBody @Valid SectionRequest createRequest,
                                         @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return sectionService.createSection(createRequest, sessionId);
    }

    @PutMapping(value = "sections/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SectionResponse renameSection(@PathVariable(value = "id") String id,
                                         @RequestBody @Valid SectionRequest renameRequest,
                                         @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int sectionId = Integer.parseInt(id);
            return sectionService.renameSection(renameRequest, sessionId, sectionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_SECTION_IDENTIFIER, id);
        }
    }

    @DeleteMapping(value = "sections/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSection(@PathVariable(value = "id") String id,
                              @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int sectionId = Integer.parseInt(id);
            sectionService.deleteSection(sessionId, sectionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_SECTION_IDENTIFIER, id);
        }
    }

    @GetMapping(value = "sections/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SectionResponse getSectionInfo(@PathVariable(value = "id") String id,
                                          @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int sectionId = Integer.parseInt(id);
            return sectionService.getSectionInfo(sessionId, sectionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_SECTION_IDENTIFIER, id);
        }

    }

    @GetMapping(value = "sections",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<SectionResponse> getAllSectionsInfo(@CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return sectionService.getAllSections(sessionId);
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
    public NoteResponse getNoteInfo(@PathVariable(value = "id") String id,
                                    @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int noteId = Integer.parseInt(id);
            return noteService.getNoteInfo(noteId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_NOTE_IDENTIFIER, id);
        }
    }

    @PutMapping(value = "notes/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse editNote(@RequestBody @Valid EditNoteRequest editNoteRequest,
                                 @PathVariable(value = "id") String id,
                                 @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int noteId = Integer.parseInt(id);
            return noteService.editNote(editNoteRequest, noteId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_NOTE_IDENTIFIER, id);
        }
    }

    @DeleteMapping(value = "notes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNote(@PathVariable(value = "id") String id,
                           @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int noteId = Integer.parseInt(id);
            noteService.deleteNote(noteId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_NOTE_IDENTIFIER, id);
        }
    }

    @PostMapping(value = "comments",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse addComment(@RequestBody @Valid CommentRequest addRequest,
                                      @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return commentService.addComment(addRequest, sessionId);
    }

    @GetMapping(value = "notes/{id}/comments",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getAllNoteComments(@PathVariable(value = "id") String id,
                                                    @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int noteId = Integer.parseInt(id);
            return commentService.getAllNoteComments(noteId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_NOTE_IDENTIFIER, id);
        }
    }

    @PutMapping(value = "comments/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse editComment(@RequestBody @Valid EditCommentRequest editRequest,
                                       @PathVariable(value = "id") String id,
                                       @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int commentId = Integer.parseInt(id);
            return commentService.editComment(editRequest, commentId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_COMMENT_IDENTIFIER, id);
        }
    }

    @DeleteMapping(value = "comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable(value = "id") String id,
                              @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int commentId = Integer.parseInt(id);
            commentService.deleteComment(commentId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_COMMENT_IDENTIFIER, id);
        }
    }

    @DeleteMapping(value = "notes/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllNoteComments(@PathVariable(value = "id") String id,
                                      @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int noteId = Integer.parseInt(id);
            noteService.deleteAllNoteComments(noteId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_NOTE_IDENTIFIER, id);
        }
    }

    @PostMapping(value = "notes/{id}/rating")
    @ResponseStatus(HttpStatus.OK)
    public void rateNote(@RequestBody @Valid RateNoteRequest rateRequest,
                         @PathVariable(value = "id") String id,
                         @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int noteId = Integer.parseInt(id);
            noteService.rateNote(rateRequest, noteId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_NOTE_IDENTIFIER, id);
        }
    }

    @GetMapping(value = "notes",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<NotesInfoResponseWithParams> getNotesInfoWithParams(@CookieValue(name = JAVASESSIONID, required = false) String sessionId,
                                                                    @RequestParam(value = "sectionId", required = false) int sectionId,
                                                                    @RequestParam(value = "sortByRating", required = false) ParamSort paramSort,
                                                                    @RequestParam(value = "tags ", required = false) List<String> tags,
                                                                    @RequestParam(value = "alltags ", required = false) boolean allTags,
                                                                    @RequestParam(value = "timeFrom", required = false) LocalDateTime timeFrom,
                                                                    @RequestParam(value = "timeTo", required = false) LocalDateTime timeTo,
                                                                    @RequestParam(value = "user", required = false) int userId,
                                                                    @RequestParam(value = "include", required = false) ParamInclude include,
                                                                    @RequestParam(value = "comments", required = false) boolean comments,
                                                                    @RequestParam(value = "allVersion", required = false) boolean allVersion,
                                                                    @RequestParam(value = "commentVersion", required = false) boolean commentVersion,
                                                                    @RequestParam(value = "from", required = false) int from,
                                                                    @RequestParam(value = "count", required = false) int count) throws NoteServerException {
        return noteService.getNotesInfo(new NoteRequestParam(sectionId, paramSort, tags, allTags, timeFrom, timeTo, userId,
                include, comments, allVersion, commentVersion, from, count), sessionId);
    }
}
