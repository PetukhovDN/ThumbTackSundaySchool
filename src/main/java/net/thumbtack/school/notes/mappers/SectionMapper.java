package net.thumbtack.school.notes.mappers;

import org.apache.ibatis.annotations.Delete;

public interface SectionMapper {


    @Delete("DELETE FROM notes.section")
    void deleteAll();
}
