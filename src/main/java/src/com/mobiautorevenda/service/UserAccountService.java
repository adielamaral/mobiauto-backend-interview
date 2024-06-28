package src.com.mobiautorevenda.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import src.com.mobiautorevenda.dto.user.UserAccountDto;
import src.com.mobiautorevenda.dto.user.request.CreateUserAccountRequest;
import src.com.mobiautorevenda.dto.user.response.UserAccountDataResponse;
import src.com.mobiautorevenda.enums.Profile;
import src.com.mobiautorevenda.exception.ExistingDataException;
import src.com.mobiautorevenda.exception.NotFoundException;
import src.com.mobiautorevenda.model.Resale;
import src.com.mobiautorevenda.model.UserAccount;
import src.com.mobiautorevenda.repository.ResaleRepository;
import src.com.mobiautorevenda.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final ResaleRepository resaleRepository;
    private final ModelMapper modelMapper;
    @Value("${mobiauto.name-super-admin-mobiauto}")
    private String nameSuperAdminMobiauto;
    @Value("${mobiauto.master-email}")
    private String masterEmail;
    @Value("${mobiauto.master-password}")
    private String masterPassword;

    public UserAccountDto findByEmail(String email) {
        Optional<UserAccount> user = userAccountRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException("Email not registered: " + email);
        }
        return modelMapper.map(user.get(), UserAccountDto.class);
    }

    @PostConstruct
    public void createSuperAdminMobiautoUserAccount() {
        final var passwordEncrypted = new DigestUtils("SHA3-256").digestAsHex(masterPassword);
        userAccountRepository.findByEmail(masterEmail).ifPresentOrElse(userAccount -> {
        }, () -> {
            var superAdmin = UserAccount.builder()
                    .name(nameSuperAdminMobiauto)
                    .email(masterEmail)
                    .password(passwordEncrypted)
                    .profile(Profile.SUPER_ADMINISTRATOR)
                    .build();
            userAccountRepository.save(superAdmin);
        });
    }

    public void createNewUserAccount(CreateUserAccountRequest dataUserAccountRequest) {
        final var checkUserRegistered = userAccountRepository.findByEmail(dataUserAccountRequest.getEmail());
        final var findResale = resaleRepository.findByCnpj(dataUserAccountRequest.getCnpjResale())
                .orElseThrow(() -> new NotFoundException("The Resale with CNPJ [" + dataUserAccountRequest.getCnpjResale() + "] does not exist."));

        if (checkUserRegistered.isPresent()) {
            throw new ExistingDataException("The user with the email [" + dataUserAccountRequest.getEmail() + "] already exists.");
        }

        final var newUserAccount = UserAccount.builder()
                .name(dataUserAccountRequest.getName())
                .email(dataUserAccountRequest.getEmail())
                .resale(Resale.builder()
                        .cnpj(findResale.getCnpj())
                        .corporateReason(findResale.getCorporateReason())
                        .build())
                .qtdServicesInProgress(0)
                .profile(dataUserAccountRequest.getProfile())
                .build();

        userAccountRepository.save(newUserAccount);
        log.info("User with email address [{}] created successfully", newUserAccount.getEmail());
    }

    public List<UserAccountDataResponse> findAllUsers() {
        List<UserAccount> allUsers = userAccountRepository.findAll();
        List<UserAccountDataResponse> listUserAccountDataResponse = new ArrayList<>();

        for (UserAccount userAccount : allUsers) {
            listUserAccountDataResponse.add(modelMapper.map(userAccount, UserAccountDataResponse.class));
        }

        return listUserAccountDataResponse;
    }

    public void updateUserAccountDataById(String userId, UserAccountDto userAccountData) {
        final var user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setName(userAccountData.getName());
        user.setEmail(userAccountData.getEmail());
        user.setProfile(userAccountData.getProfile());
        userAccountRepository.save(user);
    }

    public void deleteUserAccountById(String userId) {
        final var user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userAccountRepository.delete(user);
    }

}
