package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Comment;

import java.util.List;

public interface CommentDao {
    Integer createComment(Comment comment) throws NoteServerException;

    List<Comment> getAllCommentsForNote(int noteId);

    Integer changeComment(int commentId, String newCommentBody);

    Comment getCommentInfo(int commentId) throws NoteServerException;

    void deleteComment(int commentId);

    void deleteAllNoteComments(int noteId);
}
