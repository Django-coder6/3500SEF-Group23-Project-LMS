package com.group23.user;

import com.group23.auth.AuthService;
import com.group23.auth.InvalidCredentialsException;
import com.group23.auth.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
class UserPersistenceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void seedUserAndNativeEnumAreReadFromPostgresAndAuthenticate() {
        User user = userRepository.findByUsername("demo_staff_1").orElseThrow();

        assertThat(user.getRole()).isEqualTo(UserRole.staff);
        assertThat(user.getCollectionPointId())
                .isEqualTo(UUID.fromString("10000000-0000-4000-8000-000000000001"));
        assertThat(passwordEncoder.matches("password", user.getPasswordHash())).isTrue();

        LoginResponse response = authService.login("demo_staff_1", "password");
        assertThat(response.token()).isNotBlank();
        assertThat(response.user().roles()).containsExactly("staff");
    }

    @Test
    void repositoryFiltersStaffByCollectionPoint() {
        List<User> staff = userRepository.findAllByCollectionPointId(
                UUID.fromString("10000000-0000-4000-8000-000000000002")
        );

        assertThat(staff).extracting(User::getUsername).containsExactly("demo_staff_2");
    }

    @Test
    void registrationApprovalAndDisableRoundTripThroughPostgres() {
        String username = "resident_" + UUID.randomUUID();
        User pending = userService.registerResident(
                username,
                "test-password",
                "Integration Resident",
                "0400000000"
        );

        assertThat(pending.isActive()).isFalse();
        assertThatThrownBy(() -> authService.login(username, "test-password"))
                .isInstanceOf(InvalidCredentialsException.class);

        userService.approve(pending.getId());
        assertThat(authService.login(username, "test-password").token()).isNotBlank();

        userService.disable(pending.getId());
        assertThatThrownBy(() -> authService.login(username, "test-password"))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
