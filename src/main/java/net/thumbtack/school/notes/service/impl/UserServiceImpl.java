package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.dto.mappers.UserMapper;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.RegisterResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDaoImpl userDao;

    @Override
    public RegisterResponse registerUser(RegisterRequest userRequest) throws NoteServerException {
        log.info("Trying to register user");
        User user = UserMapper.INSTANCE.requestRegisterUser(userRequest);
        User registeredUser = userDao.registerUser(user);
        log.info("The user was registered");
        log.info("Trying to get registration response");
        RegisterResponse registrationResponse = UserMapper.INSTANCE.responseRegisterUser(registeredUser);
        log.info("Response was got");
        return registrationResponse;
    }

}
