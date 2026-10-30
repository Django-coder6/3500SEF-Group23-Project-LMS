package com.group23.location;

import com.group23.security.AuthenticatedUser;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes the collection-point location board and its manual state change. */
@RestController
@RequestMapping(path = "/api/locations", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationResponse> list(@AuthenticationPrincipal AuthenticatedUser actor) {
        return locationService.list(actor);
    }

    @PatchMapping("/{id}/status")
    public LocationResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody LocationStatusUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser actor) {
        return locationService.updateStatus(id, request, actor);
    }
}
