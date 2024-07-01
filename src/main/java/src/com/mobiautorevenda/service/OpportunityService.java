package src.com.mobiautorevenda.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import src.com.mobiautorevenda.dto.opportunity.request.OpportunityRegistrationRequest;
import src.com.mobiautorevenda.dto.opportunity.request.UpdateOpportunityRequest;
import src.com.mobiautorevenda.dto.opportunity.request.UpdateStatusOpportunityRequest;
import src.com.mobiautorevenda.dto.user.UserResponsibleDto;
import src.com.mobiautorevenda.enums.OpportunityStatus;
import src.com.mobiautorevenda.enums.Profile;
import src.com.mobiautorevenda.exception.NotFoundException;
import src.com.mobiautorevenda.exception.UnauthorizedException;
import src.com.mobiautorevenda.model.*;
import src.com.mobiautorevenda.repository.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final ClientDataRepository clientDataRepository;
    private final CarDetailsRepository carDetailsRepository;
    private final UserAccountRepository userAccountRepository;
    private final ResaleRepository resaleRepository;
    private final ModelMapper modelMapper;

    public void createNewOpportunity(OpportunityRegistrationRequest dataOpportunityRequest) {
        final var resale = resaleRepository.findByCnpj(dataOpportunityRequest.getResale().getCnpj())
                .orElseThrow(() -> new NotFoundException("Resale not found"));

        final var buildUserResponsible = findUserWithFewestServicesInProgress();

        final var buildResale = Resale.builder()
                .id(resale.getId())
                .cnpj(resale.getCnpj())
                .corporateReason(resale.getCorporateReason())
                .build();

        final var buildClientData = ClientData.builder()
                .name(dataOpportunityRequest.getClientData().getName())
                .email(dataOpportunityRequest.getClientData().getEmail())
                .phone(dataOpportunityRequest.getClientData().getPhone())
                .build();

        final var buildCarDetails = CarDetails.builder()
                .brand(dataOpportunityRequest.getCarDetails().getBrand())
                .model(dataOpportunityRequest.getCarDetails().getModel())
                .version(dataOpportunityRequest.getCarDetails().getVersion())
                .year(dataOpportunityRequest.getCarDetails().getYear())
                .build();

        final var findUser = userAccountRepository.findByEmail(buildUserResponsible.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email not found"));

        final var qtdServices = findUser.getQtdServicesInProgress();
        final var addQtdServices = qtdServices + 1;
        findUser.setQtdServicesInProgress(addQtdServices);

        final var newOpportunity = Opportunity.builder()
                .userResponsible(modelMapper.map(buildUserResponsible, UserResponsibleDto.class))
                .resale(buildResale)
                .status(dataOpportunityRequest.getStatus())
                .clientData(buildClientData)
                .carDetails(buildCarDetails)
                .attributionData(LocalDateTime.now())
                .completionDate(null)
                .build();

        clientDataRepository.save(buildClientData);
        carDetailsRepository.save(buildCarDetails);
        userAccountRepository.save(findUser);

        opportunityRepository.save(newOpportunity);
        log.info("Opportunity with ID [{}] successfully registered", newOpportunity.getId());
    }

    private UserAccount findUserWithFewestServicesInProgress() {
        UserAccount userWithFewestServices = null;
        int fewestServices = Integer.MAX_VALUE;

        final var users = userAccountRepository.findAll();
        for (UserAccount user : users) {
            if (user.getProfile() == Profile.USER) {
                int numberOfServicesInProgress = user.getQtdServicesInProgress();
                if (numberOfServicesInProgress < fewestServices) {
                    fewestServices = numberOfServicesInProgress;
                    userWithFewestServices = user;
                }
            }
        }

        if (userWithFewestServices == null) {
            throw new NotFoundException("No user with Profile.USER found to assign the service");
        }

        return userWithFewestServices;
    }

    public void update(String opportunityId, UpdateOpportunityRequest updateOpportunityRequest, Principal loggedInUser) {
        final var user = userAccountRepository.findByEmail(loggedInUser.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
        final var opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new NotFoundException("Opportunity not found"));

        if (!Objects.equals(user.getResale().getCnpj(), opportunity.getResale().getCnpj())
                && user.getProfile() != Profile.ADMINISTRATOR) {
            throw new UnauthorizedException("The user does not belong to this Resale");
        }

        if (!Objects.equals(user.getEmail(), opportunity.getUserResponsible().getEmail())
                && user.getProfile() == Profile.USER) {
            throw new UnauthorizedException("User without permission to edit this opportunity");
        }

        opportunity.setStatus(updateOpportunityRequest.getStatus());
        opportunity.setClientData(updateOpportunityRequest.getClientData());
        opportunity.setCarDetails(updateOpportunityRequest.getCarDetails());
        opportunity.setReasonForConclusion(updateOpportunityRequest.getReasonForConclusion());

        if (user.getProfile() == Profile.OWNER || user.getProfile() == Profile.MANAGER || user.getProfile() == Profile.ADMINISTRATOR) {
            opportunity.setUserResponsible(updateOpportunityRequest.getUserResponsible());
        }

        opportunityRepository.save(opportunity);
    }

    public void serviceStatusUpdate(String opportunityId, UpdateStatusOpportunityRequest updateStatusRequest) {
        final var opportunityData = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new NotFoundException("Opportunity not found"));

        opportunityData.setStatus(updateStatusRequest.getStatus());

        if (updateStatusRequest.getStatus() == OpportunityStatus.COMPLETED) {
            final var findUser = userAccountRepository.findByEmail(opportunityData.getUserResponsible().getEmail())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            opportunityData.setCompletionDate(LocalDateTime.now());
            opportunityData.setReasonForConclusion(updateStatusRequest.getReasonForConclusion());

            final var qtdServices = findUser.getQtdServicesInProgress();
            final var reduceQtdServices = qtdServices - 1;
            findUser.setQtdServicesInProgress(reduceQtdServices);
            userAccountRepository.save(findUser);
        }

        opportunityData.setCompletionDate(LocalDateTime.now());

        opportunityRepository.save(opportunityData);
    }

}
