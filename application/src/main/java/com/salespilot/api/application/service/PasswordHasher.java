package com.salespilot.api.application.service;

public interface PasswordHasher {
    boolean matches(String rawPassword, String passwordHash);

    String hash(String rawPassword);
}
