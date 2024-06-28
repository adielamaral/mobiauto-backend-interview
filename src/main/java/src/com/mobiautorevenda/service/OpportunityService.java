package src.com.mobiautorevenda.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import src.com.mobiautorevenda.dto.opportunity.request.OpportunityRegistrationRequest;
import src.com.mobiautorevenda.dto.opportunity.request.UpdateStatusOpportunityRequest;
import src.com.mobiautorevenda.enums.OpportunityStatus;
import src.com.mobiautorevenda.exception.NotFoundException;
import src.com.mobiautorevenda.model.CarDetails;
import src.com.mobiautorevenda.model.ClientData;
import src.com.mobiautorevenda.model.Opportunity;
import src.com.mobiautorevenda.model.UserAccount;
import src.com.mobiautorevenda.repository.CarDetailsRepository;
import src.com.mobiautorevenda.repository.ClientDataRepository;
import src.com.mobiautorevenda.repository.OpportunityRepository;
import src.com.mobiautorevenda.repository.UserAccountRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final ClientDataRepository clientDataRepository;
    private final CarDetailsRepository carDetailsRepository;
    private final UserAccountRepository userAccountRepository;

    public void createNewOpportunity(OpportunityRegistrationRequest dataOpportunityRequest) {

        final var buildUserResponsible = UserAccount.builder()
                .name(findUserWithFewestServicesInProgress().getName())
                .email(findUserWithFewestServicesInProgress().getEmail())
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

        final var newOpportunity = Opportunity.builder()
                .userResponsible(buildUserResponsible)
                .status(dataOpportunityRequest.getStatus())
                .clientData(buildClientData)
                .carDetails(buildCarDetails)
                .attributionData(LocalDateTime.now())
                .completionDate(null)
                .build();

        final var findUser = userAccountRepository.findByEmail(buildUserResponsible.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        final var qtdServices = findUser.getQtdServicesInProgress();
        final var addQtdServices = qtdServices + 1;
        findUser.setQtdServicesInProgress(addQtdServices);

        clientDataRepository.save(buildClientData);
        carDetailsRepository.save(buildCarDetails);
        userAccountRepository.save(findUser);

        opportunityRepository.save(newOpportunity);
        log.info("Opportunity with ID [{}] successfully registered", newOpportunity.getId());
    }

    public UserAccount findUserWithFewestServicesInProgress() {
        UserAccount userWithFewestServices = null;
        int fewestServices = Integer.MAX_VALUE;

        final var users = userAccountRepository.findAll();

        for (UserAccount user : users) {
            int numberOfServicesInProgress = user.getQtdServicesInProgress();
            if (numberOfServicesInProgress < fewestServices) {
                fewestServices = numberOfServicesInProgress;
                userWithFewestServices = user;
            }
        }

        return userWithFewestServices;
    }

    public void serviceStatusUpdate(UpdateStatusOpportunityRequest updateStatusRequest) {
        final var opportunityData = opportunityRepository.findById(updateStatusRequest.getId())
                .orElseThrow(() -> new NotFoundException("Opportunity not found"));

        opportunityData.setStatus(updateStatusRequest.getStatus());

        if (updateStatusRequest.getStatus() == OpportunityStatus.COMPLETED) {
            final var findUser = userAccountRepository.findByEmail(opportunityData.getUserResponsible().getEmail())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            opportunityData.setCompletionDate(LocalDateTime.now());

            final var qtdServices = findUser.getQtdServicesInProgress();
            final var reduceQtdServices = qtdServices - 1;
            findUser.setQtdServicesInProgress(reduceQtdServices);
            userAccountRepository.save(findUser);
        }

        opportunityData.setCompletionDate(LocalDateTime.now());

        opportunityRepository.save(opportunityData);
    }

}
