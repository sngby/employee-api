package com.example.employeeapi;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new DuplicateUserException();
        }
        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.username(),
                encodedPassword,
                "USER"
        );

        userRepository.save(user);
    }
}
