package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.servlet.http.HttpSession;

@Mapper
public interface SessionMapStruct {
    SessionMapStruct INSTANCE = Mappers.getMapper(SessionMapStruct.class);

    @Mapping(target = "sessionId", source = "id")
    Session httpSessionToNotesSession(HttpSession session);

}
