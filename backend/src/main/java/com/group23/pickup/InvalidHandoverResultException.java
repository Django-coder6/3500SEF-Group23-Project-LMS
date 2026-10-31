package com.group23.pickup;

/** Raised when Sprint 2 receives a handover result other than collected. */
public class InvalidHandoverResultException extends RuntimeException {

    public InvalidHandoverResultException() {
        super("Sprint 2 accepts only the collected handover result.");
    }
}
