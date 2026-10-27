package com.group23;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/** Starts the collection point management backend. */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class Group23Application {

    public static void main(String[] args) {
        SpringApplication.run(Group23Application.class, args);
    }
}
