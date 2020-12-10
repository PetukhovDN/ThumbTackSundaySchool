package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.UserInfoResponse;
import net.thumbtack.school.notes.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapStruct {
    UserMapStruct INSTANCE = Mappers.getMapper(UserMapStruct.class);

    User requestRegisterUser(RegisterRequest request);

    UserInfoResponse responseRegisterUser(User user);
}
