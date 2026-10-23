package com.group23.auth;

import com.group23.security.JwtService;
import com.group23.user.User;
import com.group23.user.UserRepository;
import com.group23.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService = new JwtService(
            "test-only-collection-point-jwt-secret-32-bytes",
            "collection-point-backend-test",
            Duration.ofHours(1)
    );
    private final AuthService authService =
            new AuthService(userRepository, passwordEncoder, jwtService);

    private User activeResident;

    @BeforeEach
    void setUp() {
        activeResident = mock(User.class);
        when(activeResident.getId()).thenReturn(UUID.randomUUID());
        when(activeResident.getUsername()).thenReturn("demo_resident_1");
        when(activeResident.getRole()).thenReturn(UserRole.resident);
        when(activeResident.isActive()).thenReturn(true);
        when(activeResident.getPasswordHash())
                .thenReturn(passwordEncoder.encode("correct-password"));
    }

    @Test
    void validCredentialsReturnTheContractResponse() {
        when(userRepository.findByUsername("demo_resident_1"))
                .thenReturn(Optional.of(activeResident));

        LoginResponse response = authService.login("demo_resident_1", "correct-password");

        assertThat(response.token()).isNotBlank();
        assertThat(response.user().id()).isEqualTo(activeResident.getId());
        assertThat(response.user().username()).isEqualTo("demo_resident_1");
        assertThat(response.user().roles()).containsExactly("resident");
    }

    @Test
    void wrongPasswordUnknownUserAndInactiveUserShareTheSameFailure() {
        User inactiveResident = mock(User.class);
        when(inactiveResident.isActive()).thenReturn(false);
        when(inactiveResident.getPasswordHash())
                .thenReturn(passwordEncoder.encode("correct-password"));

        when(userRepository.findByUsername("demo_resident_1"))
                .thenReturn(Optional.of(activeResident));
        when(userRepository.findByUsername("inactive"))
                .thenReturn(Optional.of(inactiveResident));

        assertThatThrownBy(() -> authService.login("demo_resident_1", "wrong"))
                .isInstanceOf(InvalidCredentialsException.class);
        assertThatThrownBy(() -> authService.login("missing", "correct-password"))
                .isInstanceOf(InvalidCredentialsException.class);
        assertThatThrownBy(() -> authService.login("inactive", "correct-password"))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
