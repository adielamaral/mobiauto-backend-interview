package src.com.mobiautorevenda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.com.mobiautorevenda.dto.resale.request.ResaleRequest;
import src.com.mobiautorevenda.enums.OpportunityStatus;
import src.com.mobiautorevenda.model.Opportunity;
import src.com.mobiautorevenda.service.ResaleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resales")
@Tag(name = "Resale", description = "Resale management")
public class ResaleController {

    private final ResaleService resaleService;

    @Operation(summary = "Create Resale")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid ResaleRequest dataResaleRequest) {
        resaleService.createNewResale(dataResaleRequest);
    }

    @Operation(summary = "Search for all opportunities by Resale ID")
    @GetMapping("/opportunities/all")
    public List<Opportunity> getAllOpportunitiesByResaleId(String resaleId) {
        return resaleService.findAllOpportunitiesByResaleId(resaleId);
    }

    @Operation(summary = "Search for all opportunities by Resale ID and Status")
    @GetMapping("/opportunities/by-status")
    public List<Opportunity> getAllOpportunitiesByResaleId(String resaleId, OpportunityStatus status) {
        return resaleService.findOpportunitiesByResaleIdAndStatus(resaleId, status);
    }

}
