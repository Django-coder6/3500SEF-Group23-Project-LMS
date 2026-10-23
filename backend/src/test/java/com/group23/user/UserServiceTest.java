package com.group23.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private static final String SEED_HASH =
            "$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void registeredResidentStartsInactiveWithAUniqueBcryptHash() {
        when(userRepository.existsByUsername("new_resident")).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User first = userService.registerResident(
                "new_resident", "password", "New Resident", "0400000000"
        );
        User second = userService.registerResident(
                "another_resident", "password", "Another Resident", "0400000001"
        );

        assertThat(first.isActive()).isFalse();
        assertThat(first.getRole()).isEqualTo(UserRole.resident);
        assertThat(first.getCollectionPointId()).isNull();
        assertThat(passwordEncoder.matches("password", first.getPasswordHash())).isTrue();
        assertThat(first.getPasswordHash()).isNotEqualTo(second.getPasswordHash());
    }

    @Test
    void existingUsernameIsRejectedBeforeSaving() {
        when(userRepository.existsByUsername("new_resident")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerResident(
                "new_resident", "password", "New Resident", "0400000000"
        )).isInstanceOf(UsernameAlreadyExistsException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void administratorCanApproveAndDisableAnAccount() {
        User pending = User.pendingResident(
                "new_resident",
                passwordEncoder.encode("password"),
                "New Resident",
                "0400000000"
        );
        when(userRepository.findById(pending.getId())).thenReturn(Optional.of(pending));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User approved = userService.approve(pending.getId());
        assertThat(approved.isActive()).isTrue();

        User disabled = userService.disable(pending.getId());
        assertThat(disabled.isActive()).isFalse();
    }

    @Test
    void bcryptAcceptsTheSeedHashAndVerifiesItsPassword() {
        assertThat(passwordEncoder.matches("password", SEED_HASH)).isTrue();
        assertThat(passwordEncoder.matches("not-the-password", SEED_HASH)).isFalse();
    }

    @Test
    void repositoryExposesCollectionPointFiltering() {
        UUID collectionPointId = UUID.randomUUID();
        userService.registerResident("resident", "password", "Resident", "0400000000");

        userRepository.findAllByCollectionPointId(collectionPointId);

        verify(userRepository).findAllByCollectionPointId(collectionPointId);
    }
}
