package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.mappers.SectionMupStruct;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.model.Section;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SectionMapStructTest {
    SectionMupStruct INSTANCE = Mappers.getMapper(SectionMupStruct.class);

    @Test
    void testTransformCreateSectionRequestToSectionModel() {
        SectionRequest createSectionRequest = new SectionRequest(
                "sectionName"
        );
        Section createdSection = INSTANCE.requestCreateSection(createSectionRequest);

        assertAll(
                () -> assertEquals(createSectionRequest.getSectionName(), createdSection.getSectionName())
        );
    }

    @Test
    void testTransformSectionModelToSectionResponse() {
        Section createdSection = new Section();
        createdSection.setId(1);
        createdSection.setSectionName("lastName");
        SectionResponse sectionResponse = INSTANCE.responseCreateSection(createdSection);

        assertAll(
                () -> assertEquals(createdSection.getId(), sectionResponse.getId()),
                () -> assertEquals(createdSection.getSectionName(), sectionResponse.getSectionName())
        );
    }
}
