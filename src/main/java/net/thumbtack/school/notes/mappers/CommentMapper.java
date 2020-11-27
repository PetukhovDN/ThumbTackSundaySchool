package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {


    @Delete("DELETE FROM note_comment")
    void deleteAll();
}
