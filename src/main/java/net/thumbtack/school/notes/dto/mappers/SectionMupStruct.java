package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct methods to transform user requests from section requests to the section models, and models to the responses
 */
@Mapper
public interface SectionMupStruct {
    SectionMupStruct INSTANCE = Mappers.getMapper(SectionMupStruct.class);

    /**
     * Transforms request to create section to the section model
     */
    @Mappings({
            @Mapping(target = "sectionName", source = "createRequest.sectionName")
    })
    Section requestCreateSection(SectionRequest createRequest);

    /**
     * Transforms created section model to the response
     */
    @Mappings({
            @Mapping(target = "id", source = "section.id"),
            @Mapping(target = "sectionName", source = "section.sectionName")
    })
    SectionResponse responseCreateSection(Section section);
}
