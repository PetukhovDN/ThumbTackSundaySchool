package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Delete;

public interface NoteMapper {


    @Delete("DELETE FROM notes.note")
    void deleteAll();
}
