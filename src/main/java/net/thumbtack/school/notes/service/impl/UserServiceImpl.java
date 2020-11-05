package net.thumbtack.school.notes.service.impl;

import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.RegisterResponse;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private UserDaoImpl userDao;

    @Autowired
    public UserServiceImpl(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public RegisterResponse registerUser(RegisterRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPatronymic(userRequest.getPatronymic());
        user.setLogin(userRequest.getLogin());
        user.setPassword(userRequest.getPassword());
        return new RegisterResponse(userDao.registerUser(user));
    }

}
