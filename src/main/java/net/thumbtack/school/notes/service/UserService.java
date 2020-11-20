package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.RegisterResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest userRequest) throws NoteServerException;
}
