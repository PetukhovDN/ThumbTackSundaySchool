package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SectionMapper {
    @Insert("INSERT INTO section (section_name, author_id) " +
            "VALUES ( #{section.sectionName}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "section.id", keyColumn = "id")
    Integer saveSection(@Param("section") Section section, int userId);

    @Select("SELECT id, section_name as sectionName, section_creation_time as creationTime, author_id " +
            "FROM section WHERE section_name = #{sectionName} ")
    @Results({
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.UserMapper.getUserById", fetchType = FetchType.LAZY))})
    Section getSectionByName(String sectionName);

    @Select("SELECT id, section_name as sectionName, section_creation_time as creationTime, author_id  " +
            "FROM section WHERE id = #{sectionId} ")
    @Results({
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.UserMapper.getUserById", fetchType = FetchType.LAZY))})
    Section getSectionById(int sectionId);

    @Update("UPDATE section SET section_name = #{newSectionName} WHERE id = #{sectionId}")
    void updateSection(int sectionId, String newSectionName);

    @Delete("DELETE FROM section WHERE id = #{sectionId}")
    void deleteSection(int sectionId);

    @Select("SELECT id, section_name as sectionName, section_creation_time as creationTime, author_id FROM section")
    @Results({
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.notes.mappers.UserMapper.getUserById", fetchType = FetchType.LAZY))})
    List<Section> getAllSections();

    @Delete("DELETE FROM section")
    void deleteAll();
}
