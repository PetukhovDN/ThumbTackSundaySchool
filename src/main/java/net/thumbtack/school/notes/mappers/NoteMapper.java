package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper {


    @Delete("DELETE FROM note")
    void deleteAll();
}
