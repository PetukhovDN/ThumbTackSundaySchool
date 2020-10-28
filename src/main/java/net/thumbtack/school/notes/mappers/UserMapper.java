package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Delete;

public interface UserMapper {


    @Delete("DELETE FROM note_user")
    void deleteAll();
}
