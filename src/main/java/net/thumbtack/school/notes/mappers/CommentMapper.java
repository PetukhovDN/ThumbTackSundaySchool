package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Delete;

public interface CommentMapper {


    @Delete("DELETE FROM notes.comment")
    void deleteAll();
}
