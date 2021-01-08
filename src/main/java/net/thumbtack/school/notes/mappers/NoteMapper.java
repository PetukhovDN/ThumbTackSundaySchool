package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO note (note_subject, author_id, section_id) " +
            "VALUES ( #{note.subject}, #{note.authorId}, #{note.sectionId})")
    @Options(useGeneratedKeys = true, keyProperty = "note.id")
    Integer saveNote(@Param("note") Note note);

    @Insert("INSERT INTO note_revision (revision_id, note_id, note_body) " +
            "VALUES ( #{noteRevision.revisionId}, #{noteRevision.noteId}, #{noteRevision.body})")
    @Options(useGeneratedKeys = true, keyProperty = "noteRevision.id")
    Integer saveNoteRevision(@Param("noteRevision") NoteRevision noteRevision);

    @Select("SELECT id, note_subject as subject, last_revision_id as lastRevisionId, " +
            "author_id as authorId, section_id as sectionId, note_creation_time as creationTime " +
            "FROM note WHERE note_subject = #{subject} ")
    @Results({
            @Result(property = "revisions", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes.mappers.NoteMapper.getAllNoteRevisions", fetchType = FetchType.LAZY))})
    Note getNoteBySubject(String subject);

    @Select("SELECT id, note_subject as subject, last_revision_id as lastRevisionId, " +
            "author_id, section_id, note_creation_time as creationTime " +
            "FROM note WHERE id = #{id} ")
    @Results({
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.UserMapper.getUserById", fetchType = FetchType.LAZY)),
            @Result(property = "section", column = "section_id", javaType = Section.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.SectionMapper.getSectionById", fetchType = FetchType.LAZY)),
            @Result(property = "revisions", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes.mappers.NoteMapper.getAllNoteRevisions", fetchType = FetchType.LAZY))})
    Note getNoteById(int id);

    @Select("SELECT id, revision_id as revisionId, note_id as noteId, note_body as body, " +
            "note_revision_creation_time as creationTime " +
            "FROM note_revision WHERE revision_id = #{revisionId} AND note_id = #{noteId}")
    NoteRevision getNoteRevision(String revisionId, int noteId);

    @Select("SELECT id, revision_id as revisionId, note_id as noteId, note_body as body, " +
            "note_revision_creation_time as creationTime " +
            "FROM note_revision WHERE note_id = #{noteId}")
    List<NoteRevision> getAllNoteRevisions(int noteId);

    @Update("UPDATE note SET last_revision_id = #{revisionId} WHERE id = #{noteId}")
    void updateNoteLastRevisionId(int noteId, String revisionId);

    @Update("UPDATE note SET section_id = #{sectionId} WHERE id = #{noteId}")
    void changeSectionId(int noteId, int sectionId);

    @Delete("DELETE FROM note WHERE id = #{noteId}")
    void deleteNoteById(int noteId);

    @Delete("DELETE FROM note")
    void deleteAll();
}
