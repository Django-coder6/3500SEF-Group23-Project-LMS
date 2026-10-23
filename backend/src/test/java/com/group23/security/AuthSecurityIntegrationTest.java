package com.group23.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group23.auth.AuthController;
import com.group23.auth.AuthService;
import com.group23.auth.InvalidCredentialsException;
import com.group23.auth.LoginResponse;
import com.group23.auth.UserSummary;
import com.group23.common.HealthController;
import com.group23.config.SecurityConfig;
import com.group23.user.User;
import com.group23.user.UserRepository;
import com.group23.user.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class, HealthController.class})
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        RestAuthenticationEntryPoint.class,
        RestAccessDeniedHandler.class,
        AuthSecurityIntegrationTest.JwtTestConfiguration.class
})
class AuthSecurityIntegrationTest {

    private static final String ISSUER = "collection-point-backend-test";
    private static final String SECRET = "test-only-collection-point-jwt-secret-32-bytes";
    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID =
            UUID.fromString("10000000-0000-4000-8000-000000000001");

    private static final List<ProtectedEndpoint> PROTECTED_ENDPOINTS = List.of(
            new ProtectedEndpoint(HttpMethod.GET, "/api/parcels",
                    Set.of(UserRole.staff, UserRole.owner, UserRole.admin)),
            new ProtectedEndpoint(HttpMethod.POST, "/api/parcels", Set.of(UserRole.staff)),
            new ProtectedEndpoint(HttpMethod.POST, "/api/parcels/batch", Set.of(UserRole.staff)),
            new ProtectedEndpoint(HttpMethod.PATCH,
                    "/api/parcels/40000000-0000-4000-8000-000000000001/status",
                    Set.of(UserRole.staff)),
            new ProtectedEndpoint(HttpMethod.GET, "/api/my-parcels", Set.of(UserRole.resident)),
            new ProtectedEndpoint(HttpMethod.GET,
                    "/api/my-parcels/40000000-0000-4000-8000-000000000001",
                    Set.of(UserRole.resident)),
            new ProtectedEndpoint(HttpMethod.GET,
                    "/api/my-parcels/40000000-0000-4000-8000-000000000001/pickup-code",
                    Set.of(UserRole.resident)),
            new ProtectedEndpoint(HttpMethod.GET, "/api/locations",
                    Set.of(UserRole.staff, UserRole.owner, UserRole.admin)),
            new ProtectedEndpoint(HttpMethod.POST, "/api/locations/recommend",
                    Set.of(UserRole.staff)),
            new ProtectedEndpoint(HttpMethod.POST, "/api/pickup/verify", Set.of(UserRole.staff)),
            new ProtectedEndpoint(HttpMethod.POST, "/api/pickup/confirm", Set.of(UserRole.staff))
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthService authService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void validLoginReturnsOnlyTheFrozenResponseFieldsAndIgnoresIdentityFields() throws Exception {
        UUID userId = UUID.randomUUID();
        when(authService.login("demo_resident_1", "password"))
                .thenReturn(new LoginResponse(
                        "signed-token",
                        new UserSummary(userId, "demo_resident_1", List.of("resident"))
                ));

        mockMvc.perform(request(HttpMethod.POST, "/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "demo_resident_1",
                                  "password": "password",
                                  "operatorId": "forged",
                                  "actor": "forged",
                                  "owner": "forged"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("signed-token"))
                .andExpect(jsonPath("$.user.id").value(userId.toString()))
                .andExpect(jsonPath("$.user.username").value("demo_resident_1"))
                .andExpect(jsonPath("$.user.roles[0]").value("resident"))
                .andExpect(jsonPath("$.user.roles.length()").value(1))
                .andExpect(jsonPath("$.user.collectionPointId").doesNotExist());

        verify(authService).login("demo_resident_1", "password");
    }

    @Test
    void invalidLoginUsesTheSharedUnauthorizedErrorShape() throws Exception {
        when(authService.login(anyString(), anyString()))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(request(HttpMethod.POST, "/api/auth/login")
                        .header("X-Request-Id", "request-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"missing","password":"wrong"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
                .andExpect(jsonPath("$.error.message").value("Invalid username or password."))
                .andExpect(jsonPath("$.error.requestId").value("request-123"));
    }

    @Test
    void everyFrozenProtectedEndpointRejectsAnonymousRequests() throws Exception {
        for (ProtectedEndpoint endpoint : PROTECTED_ENDPOINTS) {
            mockMvc.perform(request(endpoint.method(), endpoint.path()))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
        }
    }

    @Test
    void completeRoleMatrixIsAllowedOrForbiddenForEveryFrozenEndpoint() throws Exception {
        Map<UserRole, String> tokens = Arrays.stream(UserRole.values())
                .collect(Collectors.toMap(role -> role, this::tokenForRole));

        for (ProtectedEndpoint endpoint : PROTECTED_ENDPOINTS) {
            for (UserRole role : UserRole.values()) {
                var result = mockMvc.perform(request(endpoint.method(), endpoint.path())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(role)))
                        .andReturn();

                if (endpoint.allowedRoles().contains(role)) {
                    assertThat(result.getResponse().getStatus())
                            .as("%s %s should allow %s", endpoint.method(), endpoint.path(), role)
                            .isNotIn(401, 403);
                } else {
                    assertThat(result.getResponse().getStatus())
                            .as("%s %s should forbid %s", endpoint.method(), endpoint.path(), role)
                            .isEqualTo(403);
                }
            }
        }
    }

    @Test
    void malformedAndDisabledUserTokensAreRejected() throws Exception {
        mockMvc.perform(request(HttpMethod.GET, "/api/parcels")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    @Test
    void disabledUserLosesAccessImmediatelyEvenWhenTheTokenIsStillValid() throws Exception {
        User user = user(UserRole.staff, STAFF_ID, true);
        String token = jwtService.issue(user);
        when(user.isActive()).thenReturn(false);
        when(userRepository.findById(STAFF_ID)).thenReturn(Optional.of(user));

        mockMvc.perform(request(HttpMethod.GET, "/api/parcels")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticationPrincipalComesFromTheCurrentDatabaseRow() throws Exception {
        User user = user(UserRole.staff, STAFF_ID, true);
        String token = jwtService.issue(user);
        JwtService.JwtClaims claims = jwtService.parse(token);
        when(userRepository.findById(STAFF_ID)).thenReturn(Optional.of(user));

        assertThat(claims.userId()).isEqualTo(STAFF_ID);
        assertThat(claims.role()).isEqualTo(UserRole.staff);
        assertThat(claims.collectionPointId()).isEqualTo(COLLECTION_POINT_ID);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(
                jwtService,
                userRepository,
                new RestAuthenticationEntryPoint(new ObjectMapper())
        );
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/parcels");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getPrincipal()).isInstanceOf(AuthenticatedUser.class);
        AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
        assertThat(principal.userId()).isEqualTo(STAFF_ID);
        assertThat(principal.role()).isEqualTo(UserRole.staff);
        assertThat(principal.collectionPointId()).isEqualTo(COLLECTION_POINT_ID);
    }

    @Test
    void allBusinessMvcMappingsAreTheTwoDocumentedPublicMappings() {
        Set<String> mappings = handlerMapping.getHandlerMethods().keySet().stream()
                .flatMap(this::mappingStrings)
                .filter(mapping -> mapping.contains(" /api/"))
                .collect(Collectors.toSet());

        assertThat(mappings).containsExactlyInAnyOrder(
                "GET /api/health",
                "POST /api/auth/login"
        );
    }

    private java.util.stream.Stream<String> mappingStrings(RequestMappingInfo info) {
        Set<org.springframework.web.bind.annotation.RequestMethod> methods =
                info.getMethodsCondition().getMethods();
        Set<String> patterns = info.getPathPatternsCondition().getPatternValues();
        if (methods.isEmpty()) {
            return patterns.stream().map(pattern -> "* " + pattern);
        }
        return methods.stream()
                .flatMap(method -> patterns.stream().map(pattern -> method + " " + pattern));
    }

    private String tokenForRole(UserRole role) {
        UUID userId = UUID.nameUUIDFromBytes(role.name().getBytes());
        User user = user(role, userId, true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        return jwtService.issue(user);
    }

    private User user(UserRole role, UUID id, boolean active) {
        User user = mock(User.class);
        when(user.getId()).thenReturn(id);
        when(user.getUsername()).thenReturn("user-" + role);
        when(user.getRole()).thenReturn(role);
        when(user.isActive()).thenReturn(active);
        when(user.getCollectionPointId())
                .thenReturn(role == UserRole.staff ? COLLECTION_POINT_ID : null);
        return user;
    }

    private record ProtectedEndpoint(
            HttpMethod method,
            String path,
            Set<UserRole> allowedRoles
    ) {
    }

    @TestConfiguration
    static class JwtTestConfiguration {

        @Bean
        JwtService jwtService() {
            return new JwtService(SECRET, ISSUER, Duration.ofHours(1));
        }
    }
}
