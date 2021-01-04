package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UsersInfoResponse;
import net.thumbtack.school.notes.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct methods to transform user requests to the models and models to the user responses
 */
@Mapper
public interface UserMapStruct {
    UserMapStruct INSTANCE = Mappers.getMapper(UserMapStruct.class);

    /**
     * Transforms request to register user to the user model
     */
    User requestRegisterUser(RegisterRequest request);

    /**
     * Transforms registered user model to response
     */
    UserInfoResponse responseRegisterUser(User user);

    /**
     * Transforms request to update user information to the user model
     */
    User requestUpdateUser(UpdateUserInfoRequest updateUserInfoRequest);

    /**
     * Transforms updated user model to response
     */
    UpdateUserInfoResponse responseUpdateUserInfo(User user);

    /**
     * Transforms user models to response for returning list of users
     */
    UsersInfoResponse responseGetAllUsers(User user);
}
