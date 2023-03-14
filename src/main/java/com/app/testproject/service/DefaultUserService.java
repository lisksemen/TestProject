package com.app.testproject.service;

import com.app.testproject.dto.UserDTO;
import com.app.testproject.dto.UserRequest;
import com.app.testproject.model.User;
import com.app.testproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userRepository.getReferenceById(id);
            return Optional.of(convertToUserDTO(user));
        }
        return Optional.empty();
    }

    @Override
    public User addUser(UserRequest userRequest) {
        User user = convertToUser(userRequest);

        userRepository.save(user);
        return user;
    }

    private User convertToUser(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .dateOfBirth(convertToLocalDateFromString(userRequest.getDateOfBirth()))
                .build();
    }

    private int getAgeFromDateOfBirth(LocalDate dateOfBirth) {
        return Math.toIntExact(ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now()));
    }

    private LocalDate convertToLocalDateFromString(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, dateTimeFormatter);
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(getAgeFromDateOfBirth(user.getDateOfBirth()))
                .build();
    }
}
