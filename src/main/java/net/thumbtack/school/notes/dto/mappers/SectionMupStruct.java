package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SectionMupStruct {
    SectionMupStruct INSTANCE = Mappers.getMapper(SectionMupStruct.class);

    Section requestCreateSection(SectionRequest createRequest);

    SectionResponse responseCreateSection(Section section);
}
