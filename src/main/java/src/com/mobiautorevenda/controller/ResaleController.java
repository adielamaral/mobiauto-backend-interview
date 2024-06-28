package src.com.mobiautorevenda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.com.mobiautorevenda.dto.resale.ResaleDto;
import src.com.mobiautorevenda.service.ResaleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resale")
@Tag(name = "Resale", description = "Resale management")
public class ResaleController {

    private final ResaleService resaleService;

    @Operation(summary = "Create resale")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid ResaleDto dataResaleRequest) {
        resaleService.createNewResale(dataResaleRequest);
    }

}
