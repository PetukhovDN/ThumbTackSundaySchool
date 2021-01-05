package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Section;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SectionMapper {

    @Insert("INSERT INTO section (section_name, author_id) " +
            "VALUES ( #{section.sectionName}, #{section.authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "section.id")
    Integer saveSection(@Param("section") Section section);

    @Select("SELECT id, section_name as sectionName, section_creation_time as creationTime, author_id as authorId " +
            "FROM section WHERE section_name = #{sectionName} ")
    Section getSectionByName(String sectionName);

    @Select("SELECT id, section_name as sectionName, section_creation_time as creationTime, author_id as authorId " +
            "FROM section WHERE id = #{sectionId} ")
    Section getSectionById(long sectionId);

    @Update("UPDATE section SET section_name = #{newSectionName} WHERE id = #{sectionId}")
    void updateSection(long sectionId, String newSectionName);

    @Delete("DELETE FROM section WHERE id = #{sectionId}")
    void deleteSection(long sectionId);

    @Select("SELECT id, section_name as sectionName, section_creation_time as creationTime, author_id as authorId FROM section")
    List<Section> getAllSections();

    @Delete("DELETE FROM section")
    void deleteAll();
}
