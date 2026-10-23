package com.group23.auth;

import com.group23.security.JwtService;
import com.group23.user.User;
import com.group23.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    static final String DUMMY_PASSWORD_HASH =
            "$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        String passwordHash = user == null ? DUMMY_PASSWORD_HASH : user.getPasswordHash();
        boolean passwordMatches = passwordEncoder.matches(password, passwordHash);

        if (user == null || !user.isActive() || !passwordMatches) {
            throw new InvalidCredentialsException();
        }

        return new LoginResponse(jwtService.issue(user), UserSummary.from(user));
    }
}
