package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.model.Comment;

import java.util.List;

public interface CommentDao {
    Comment createComment(String token, Comment comment);

    List<Comment> getAllCommentsForNote(String token, int noteId);

    Comment changeComment(String token, int commentId, String newCommentBody);

    void deleteComment(String token, int commentId);
}
