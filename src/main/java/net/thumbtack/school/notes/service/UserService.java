package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.RegisterResponse;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest userRequest);
}
