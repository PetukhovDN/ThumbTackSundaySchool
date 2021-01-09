package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.mappers.CommentMapStruct;
import net.thumbtack.school.notes.dto.request.comment.CommentRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentResponse;
import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapStructTest {
    CommentMapStruct INSTANCE = Mappers.getMapper(CommentMapStruct.class);

    @Test
    void testTransformAddCommentRequestToCommentModel() {
        CommentRequest createdCommentRequest = new CommentRequest(
                "comment_body",
                1
        );
        Comment createdComment = INSTANCE.requestAddComment(createdCommentRequest);

        assertAll(
                () -> assertEquals(createdCommentRequest.getBody(), createdComment.getCommentBody()),
                () -> assertEquals(createdCommentRequest.getNoteId(), createdComment.getNote().getId())
        );
    }

    @Test
    void testTransformCommentModelToCommentAddResponse() {
        Note note = new Note();
        note.setId(1);
        User author = new User();
        author.setId(2);
        NoteRevision noteRevision = new NoteRevision();
        noteRevision.setNoteId(3);

        Comment comment = new Comment();
        comment.setId(3);
        comment.setNote(note);
        comment.setAuthor(author);
        comment.setNoteRevision(noteRevision);
        comment.setCreationTime(LocalDateTime.now());
        CommentResponse commentResponse = INSTANCE.responseAddComment(comment);

        assertAll(
                () -> assertEquals(comment.getId(), commentResponse.getId()),
                () -> assertEquals(comment.getCommentBody(), commentResponse.getBody()),
                () -> assertEquals(comment.getNote().getId(), commentResponse.getNoteId()),
                () -> assertEquals(comment.getAuthor().getId(), commentResponse.getAuthorId()),
                () -> assertEquals(comment.getCreationTime(), commentResponse.getCreated()),
                () -> assertEquals(comment.getNoteRevision().getRevisionId(), commentResponse.getRevisionId())
        );
    }
}
