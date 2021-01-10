package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO note_comment (comment_body, author_id, note_id, revision_id) " +
            "VALUES ( #{comment.commentBody}, #{comment.author.id}, #{comment.note.id}, #{comment.note.lastRevisionId})")
    @Options(useGeneratedKeys = true, keyProperty = "comment.id", keyColumn = "id")
    Integer saveComment(@Param("comment") Comment comment);

    @Select("SELECT id, comment_body as commentBody, author_id, note_id, revision_id, " +
            "comment_creation_time as creationTime FROM note_comment WHERE id = #{id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.UserMapper.getUserById", fetchType = FetchType.LAZY)),
            @Result(property = "note", column = "note_id", javaType = Note.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.NoteMapper.getNoteById", fetchType = FetchType.LAZY)),
            @Result(property = "noteRevision", column = "revision_id", javaType = NoteRevision.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.NoteMapper.getNoteRevision", fetchType = FetchType.LAZY))})
    Comment getCommentById(int commentId);

    @Select("SELECT id, comment_body as commentBody, author_id, note_id, revision_id, " +
            "comment_creation_time as creationTime FROM note_comment WHERE note_id = #{noteId} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.UserMapper.getUserById", fetchType = FetchType.LAZY)),
            @Result(property = "note", column = "note_id", javaType = Note.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.NoteMapper.getNoteById", fetchType = FetchType.LAZY)),
            @Result(property = "noteRevision", column = "revision_id", javaType = NoteRevision.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.NoteMapper.getNoteRevision", fetchType = FetchType.LAZY))})
    List<Comment> getAllCommentsForNote(int noteId);

    @Update("UPDATE note_comment SET comment_body = #{newCommentBody} WHERE id = #{commentId}")
    void updateCommentText(int commentId, String newCommentBody);

    @Delete("DELETE FROM note_comment WHERE id = #{commentId}")
    void deleteCommentById(int commentId);

    @Delete("DELETE FROM note_comment WHERE note_id = #{noteId}")
    void deleteCommentsForNote(int noteId);

    @Delete("DELETE FROM note_comment")
    void deleteAll();
}
