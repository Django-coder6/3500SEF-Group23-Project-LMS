package com.group23.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerResident(
            String username,
            String rawPassword,
            String fullName,
            String phone
    ) {
        String normalizedUsername = requireText(username, "username");
        String normalizedFullName = requireText(fullName, "fullName");
        String normalizedPhone = requireText(phone, "phone");
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("password must not be blank");
        }
        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new UsernameAlreadyExistsException(normalizedUsername);
        }

        User user = User.pendingResident(
                normalizedUsername,
                passwordEncoder.encode(rawPassword),
                normalizedFullName,
                normalizedPhone
        );
        return userRepository.save(user);
    }

    @Transactional
    public User approve(UUID userId) {
        User user = requireUser(userId);
        user.activate();
        return userRepository.save(user);
    }

    @Transactional
    public User disable(UUID userId) {
        User user = requireUser(userId);
        user.disable();
        return userRepository.save(user);
    }

    private User requireUser(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }
}
