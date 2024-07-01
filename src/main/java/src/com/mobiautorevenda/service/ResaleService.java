package src.com.mobiautorevenda.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import src.com.mobiautorevenda.dto.resale.request.ResaleRequest;
import src.com.mobiautorevenda.enums.OpportunityStatus;
import src.com.mobiautorevenda.exception.ExistingDataException;
import src.com.mobiautorevenda.exception.InvalidDataException;
import src.com.mobiautorevenda.model.Opportunity;
import src.com.mobiautorevenda.model.Resale;
import src.com.mobiautorevenda.repository.OpportunityRepository;
import src.com.mobiautorevenda.repository.ResaleRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ResaleService {

    private final ResaleRepository resaleRepository;
    private final OpportunityRepository opportunityRepository;

    public void createNewResale(ResaleRequest dataResaleRequest) {
        if (!validateCnpj(dataResaleRequest.getCnpj())) {
            throw new InvalidDataException("Invalid CNPJ");
        }

        final var checkResaleRegistered = resaleRepository.findByCnpj(dataResaleRequest.getCnpj());

        if (checkResaleRegistered.isPresent()) {
            throw new ExistingDataException("This Resale with CNPJ [" + dataResaleRequest.getCnpj() + "] already exists.");
        }

        final var newResale = Resale.builder()
                .cnpj(dataResaleRequest.getCnpj())
                .corporateReason(dataResaleRequest.getCorporateReason())
                .build();

        resaleRepository.save(newResale);
        log.info("Resale with CNPJ [{}] created successfully", newResale.getCnpj());
    }

    private boolean validateCnpj(String cnpj) {
        String cleanedCNPJ = cnpj.replaceAll("[^\\d]", "");

        if (cleanedCNPJ.length() != 14) {
            return false;
        }

        if (cleanedCNPJ.matches("(\\d)\\1{13}")) {
            return false;
        }

        int sum = 0;
        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(cleanedCNPJ.charAt(i));
            sum += digit * weight1[i];
        }
        int result = sum % 11;
        result = result < 2 ? 0 : 11 - result;

        if (result != Character.getNumericValue(cleanedCNPJ.charAt(12))) {
            return false;
        }

        sum = 0;
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 13; i++) {
            int digit = Character.getNumericValue(cleanedCNPJ.charAt(i));
            sum += digit * weight2[i];
        }
        result = sum % 11;
        result = result < 2 ? 0 : 11 - result;

        return result == Character.getNumericValue(cleanedCNPJ.charAt(13));
    }

    public List<Opportunity> findAllOpportunitiesByResaleId(String resaleId) {
        return opportunityRepository.findAllByResaleId(resaleId);
    }

    public List<Opportunity> findOpportunitiesByResaleIdAndStatus(String resaleId, OpportunityStatus status) {
        return opportunityRepository.findAllByResaleIdAndStatus(resaleId, status);
    }

}
