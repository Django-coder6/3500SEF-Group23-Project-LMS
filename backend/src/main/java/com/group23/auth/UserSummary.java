package com.group23.auth;

import com.group23.user.User;

import java.util.List;
import java.util.UUID;

public record UserSummary(
        UUID id,
        String username,
        List<String> roles
) {
    public static UserSummary from(User user) {
        return new UserSummary(
                user.getId(),
                user.getUsername(),
                List.of(user.getRole().name())
        );
    }
}
