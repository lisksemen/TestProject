package com.app.testproject.service;

import com.app.testproject.dto.UserDTO;
import com.app.testproject.dto.UserRequest;
import com.app.testproject.model.User;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUserById(Long id);

    User addUser(UserRequest userRequest);
}
