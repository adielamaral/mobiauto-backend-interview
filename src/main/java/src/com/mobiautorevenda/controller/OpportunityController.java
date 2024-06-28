package src.com.mobiautorevenda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.com.mobiautorevenda.dto.opportunity.request.OpportunityRegistrationRequest;
import src.com.mobiautorevenda.dto.opportunity.request.UpdateStatusOpportunityRequest;
import src.com.mobiautorevenda.service.OpportunityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/opportunities")
@Tag(name = "Opportunity", description = "Opportunity management")
public class OpportunityController {

    private final OpportunityService opportunityService;

    @Operation(summary = "Registration of opportunities")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid OpportunityRegistrationRequest dataOpportunityRequest) {
        opportunityService.createNewOpportunity(dataOpportunityRequest);
    }

    @Operation(summary = "Service status update")
    @PutMapping
    public void updateStatus(@RequestBody @Valid UpdateStatusOpportunityRequest updateStatusOpportunityRequest) {
        opportunityService.serviceStatusUpdate(updateStatusOpportunityRequest);
    }

}
