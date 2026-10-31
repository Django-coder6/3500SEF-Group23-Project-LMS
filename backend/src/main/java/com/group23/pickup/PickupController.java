package com.group23.pickup;

import com.group23.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes staff pickup verification and handover confirmation. */
@RestController
@RequestMapping(path = "/api/pickup", produces = MediaType.APPLICATION_JSON_VALUE)
public class PickupController {

    private final PickupVerificationService pickupVerificationService;
    private final PickupConfirmationService pickupConfirmationService;

    public PickupController(
            PickupVerificationService pickupVerificationService, PickupConfirmationService pickupConfirmationService) {
        this.pickupVerificationService = pickupVerificationService;
        this.pickupConfirmationService = pickupConfirmationService;
    }

    @PostMapping("/verify")
    public PickupVerifyResponse verify(
            @Valid @RequestBody PickupVerifyRequest request, @AuthenticationPrincipal AuthenticatedUser actor) {
        return pickupVerificationService.verify(request, actor);
    }

    @PostMapping("/confirm")
    public HandoverResponse confirm(
            @Valid @RequestBody PickupConfirmationRequest request, @AuthenticationPrincipal AuthenticatedUser actor) {
        return pickupConfirmationService.confirm(request, actor);
    }
}
