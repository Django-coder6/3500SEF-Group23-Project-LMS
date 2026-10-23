package com.group23.security;

import com.group23.user.User;
import com.group23.user.UserRole;

import java.security.Principal;
import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        UserRole role,
        UUID collectionPointId
) implements Principal {

    public static AuthenticatedUser from(User user) {
        return new AuthenticatedUser(
                user.getId(),
                user.getRole(),
                user.getCollectionPointId()
        );
    }

    @Override
    public String getName() {
        return userId.toString();
    }
}
