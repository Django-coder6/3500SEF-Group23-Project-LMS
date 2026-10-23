package com.group23.auth;

public record LoginResponse(String token, UserSummary user) {
}
