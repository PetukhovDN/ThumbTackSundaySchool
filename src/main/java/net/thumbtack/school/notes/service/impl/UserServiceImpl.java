package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.dto.mappers.UserMapper;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.RegisterResponse;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDaoImpl userDao;

    @Override
    public RegisterResponse registerUser(RegisterRequest userRequest) {
        log.info("Trying to register user");
        User user = UserMapper.INSTANCE.requestToUser(userRequest);
        RegisterResponse registerResponse = UserMapper.INSTANCE.userToResponse(userDao.registerUser(user));
        log.info("The user was registered");
        return registerResponse;
    }

}
