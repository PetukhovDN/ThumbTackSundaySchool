package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UsersInfoResponse;
import net.thumbtack.school.notes.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct methods to transform user requests from user controller to the models and models to the responses
 */
@Mapper
public interface UserMapStruct {
    UserMapStruct INSTANCE = Mappers.getMapper(UserMapStruct.class);

    /**
     * Transforms request to register user to the user model
     */
    @Mappings({
            @Mapping(target = "firstName", source = "request.firstName"),
            @Mapping(target = "lastName", source = "request.lastName"),
            @Mapping(target = "patronymic", source = "request.patronymic"),
            @Mapping(target = "login", source = "request.login"),
            @Mapping(target = "password", source = "request.password")
    })
    User requestRegisterUser(RegisterRequest request);

    /**
     * Transforms registered user model to response
     */
    @Mappings({
            @Mapping(target = "firstName", source = "user.firstName"),
            @Mapping(target = "lastName", source = "user.lastName"),
            @Mapping(target = "patronymic", source = "user.patronymic"),
            @Mapping(target = "login", source = "user.login")
    })
    UserInfoResponse responseRegisterUser(User user);

    /**
     * Transforms request to update user information to the user model
     */
    @Mappings({
            @Mapping(target = "firstName", source = "updateUserInfoRequest.firstName"),
            @Mapping(target = "lastName", source = "updateUserInfoRequest.lastName"),
            @Mapping(target = "patronymic", source = "updateUserInfoRequest.patronymic"),
            @Mapping(target = "password", source = "updateUserInfoRequest.newPassword")
    })
    User requestUpdateUser(UpdateUserInfoRequest updateUserInfoRequest);

    /**
     * Transforms updated user model to response
     */
    @Mappings({
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "firstName", source = "user.firstName"),
            @Mapping(target = "lastName", source = "user.lastName"),
            @Mapping(target = "patronymic", source = "user.patronymic"),
            @Mapping(target = "login", source = "user.login")
    })
    UpdateUserInfoResponse responseUpdateUserInfo(User user);

    /**
     * Transforms user models to response for returning list of users
     */
    @Mappings({
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "firstName", source = "user.firstName"),
            @Mapping(target = "lastName", source = "user.lastName"),
            @Mapping(target = "patronymic", source = "user.patronymic"),
            @Mapping(target = "login", source = "user.login"),
            @Mapping(target = "creationTime", source = "user.creationTime"),
            @Mapping(target = "online", source = "user.online"),
            @Mapping(target = "deleted", source = "user.deleted"),
            @Mapping(target = "userStatus", source = "user.userStatus"),
            @Mapping(target = "rating", source = "user.rating")
    })
    UsersInfoResponse responseGetAllUsers(User user);
}
