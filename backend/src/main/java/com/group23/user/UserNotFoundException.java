package com.group23.user;

import java.util.UUID;

/** Signals that the requested user account does not exist. */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("User not found: " + userId);
    }
}
