package com.group23.parcel;

import com.group23.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Exposes manual single and batch parcel registration endpoints. */
@RestController
@RequestMapping(path = "/api/parcels", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParcelController {

    private final ParcelRegistrationService registrationService;
    private final ParcelBatchService batchService;

    public ParcelController(ParcelRegistrationService registrationService, ParcelBatchService batchService) {
        this.registrationService = registrationService;
        this.batchService = batchService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParcelResponse register(
            @Valid @RequestBody ParcelCreateRequest request, @AuthenticationPrincipal AuthenticatedUser actor) {
        return registrationService.register(request, actor);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public ParcelBatchResult registerBatch(
            @Valid @RequestBody ParcelBatchCreateRequest request, @AuthenticationPrincipal AuthenticatedUser actor) {
        return batchService.register(request, actor);
    }
}
