package src.com.mobiautorevenda.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import src.com.mobiautorevenda.dto.resale.ResaleDto;
import src.com.mobiautorevenda.exception.NonMatchingPasswordsException;
import src.com.mobiautorevenda.model.Resale;
import src.com.mobiautorevenda.repository.ResaleRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class ResaleService {

    private final ResaleRepository resaleRepository;

    public void createNewResale(ResaleDto dataResaleRequest) {
        final var checkResaleRegistered = resaleRepository.findByCnpj(dataResaleRequest.getCnpj());

        if (checkResaleRegistered.isPresent()) {
            throw new NonMatchingPasswordsException("This Resale with CNPJ [" + dataResaleRequest.getCnpj() + "] already exists.");
        }

        final var newResale = Resale.builder()
                .cnpj(dataResaleRequest.getCnpj())
                .corporateReason(dataResaleRequest.getCorporateReason())
                .build();

        resaleRepository.save(newResale);
        log.info("Resale with CNPJ [{}] created successfully", newResale.getCnpj());
    }

}
