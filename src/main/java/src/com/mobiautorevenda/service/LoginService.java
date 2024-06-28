package src.com.mobiautorevenda.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import src.com.mobiautorevenda.dto.login.request.LoginRequest;
import src.com.mobiautorevenda.dto.login.request.SetPasswordRequest;
import src.com.mobiautorevenda.dto.login.response.UserLoginResponse;
import src.com.mobiautorevenda.dto.password.PasswordCodeDataDto;
import src.com.mobiautorevenda.dto.user.UserAccountDto;
import src.com.mobiautorevenda.exception.BadCredentialsException;
import src.com.mobiautorevenda.exception.InvalidCodeException;
import src.com.mobiautorevenda.exception.NotFoundException;
import src.com.mobiautorevenda.model.PasswordCodeData;
import src.com.mobiautorevenda.model.UserAccount;
import src.com.mobiautorevenda.repository.PasswordCodeDataRepository;
import src.com.mobiautorevenda.repository.UserAccountRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {

    public static final String SHA_3_256 = "SHA3-256";
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;
    private final PasswordCodeDataRepository passwordCodeDataRepository;
    private final ModelMapper modelMapper;

    public UserLoginResponse login(LoginRequest loginRequest) {
        final var user = userAccountService.findByEmail(formatAndValidateEmail(loginRequest.getEmail()));
        final var passwordLoginEncrypted = new DigestUtils(SHA_3_256).digestAsHex(loginRequest.getPassword());
        String userPassword = user.getPassword();

        if (Objects.equals(user.getEmail(), loginRequest.getEmail()) && Objects.isNull(userPassword)) {
            return codeForSetFirstPassword(user.getEmail());
        } else if (!Objects.equals(user.getPassword(), passwordLoginEncrypted)) {
            throw new BadCredentialsException("Invalid login, email/password combination is wrong");
        }

        return getUserLoginResponse(user);
    }

    private String formatAndValidateEmail(String email) {
        return email.trim().replaceAll("\\s+", " ");
    }

    private UserLoginResponse getUserLoginResponse(UserAccountDto userData) {
        return UserLoginResponse.builder()
                .user(userData)
                .token(jwtService.generateToken(userData))
                .build();
    }

    public UserLoginResponse codeForSetFirstPassword(String email) {
        final var userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not registered: " + email));
        String generatedCode = RandomStringUtils.randomNumeric(4);

        final var passwordCodeData = PasswordCodeDataDto.builder()
                .email(userAccount.getEmail())
                .code(generatedCode)
                .valid(true)
                .createdAt(LocalDateTime.now())
                .build();

        passwordCodeDataRepository.save(modelMapper.map(passwordCodeData, PasswordCodeData.class));

        return UserLoginResponse.builder()
                .registrationPasswordCode(generatedCode)
                .build();
    }

    public void setPassword(SetPasswordRequest setPasswordRequest) {
        validateEmailExists(setPasswordRequest.getEmail());
        validatePasswordCode(setPasswordRequest);

        final var preRegisteredUser = userAccountRepository.findByEmail(setPasswordRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (preRegisteredUser.getPassword() != null) {
            throw new InvalidCodeException("To set the password, you must use the code that was sent or log in for the first time");
        }

        preRegisteredUser.setPassword(encryptPassword(setPasswordRequest.getPassword()));
        userAccountRepository.save(preRegisteredUser);

        invalidatePasswordCode(preRegisteredUser.getEmail());
    }

    private void validateEmailExists(String email) {
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findByEmail(email);
        if (optionalUserAccount.isEmpty()) {
            throw new NotFoundException("Email not registered: " + email);
        }
    }

    private void validatePasswordCode(SetPasswordRequest setPasswordRequest) {
        PasswordCodeData passwordCodeData = passwordCodeDataRepository.findByEmail(setPasswordRequest.getEmail());
        if (passwordCodeData == null || !passwordCodeData.isValid() || !Objects.equals(setPasswordRequest.getCode(), passwordCodeData.getCode())) {
            throw new InvalidCodeException("No valid password code found for the given email or invalid code");
        }
    }

    private void invalidatePasswordCode(String email) {
        PasswordCodeData passwordCodeData = passwordCodeDataRepository.findByEmail(email);
        if (passwordCodeData != null) {
            passwordCodeData.setValid(false);
            passwordCodeDataRepository.save(passwordCodeData);
        }
    }

    private String encryptPassword(String password) {
        return new DigestUtils(SHA_3_256).digestAsHex(password);
    }

}
