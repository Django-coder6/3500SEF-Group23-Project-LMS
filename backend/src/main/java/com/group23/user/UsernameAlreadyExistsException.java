package com.group23.user;

/** Signals an attempt to create an account with an existing username. */
public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}
