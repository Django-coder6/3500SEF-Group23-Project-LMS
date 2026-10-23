package com.group23.common;

import com.group23.config.SecurityConfig;
import com.group23.security.JwtAuthenticationFilter;
import com.group23.security.JwtService;
import com.group23.security.RestAccessDeniedHandler;
import com.group23.security.RestAuthenticationEntryPoint;
import com.group23.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthController.class)
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        RestAuthenticationEntryPoint.class,
        RestAccessDeniedHandler.class,
        HealthControllerTest.JwtTestConfiguration.class
})
class HealthControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthEndpointIsPublicAndMatchesTheContract() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"status":"ok"}
                        """, true));
    }

    @TestConfiguration
    static class JwtTestConfiguration {

        @Bean
        JwtService jwtService() {
            return new JwtService(
                    "test-only-collection-point-jwt-secret-32-bytes",
                    "collection-point-backend-test",
                    Duration.ofHours(1)
            );
        }
    }
}
