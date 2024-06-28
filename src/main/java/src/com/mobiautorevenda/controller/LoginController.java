package src.com.mobiautorevenda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.com.mobiautorevenda.dto.login.request.LoginRequest;
import src.com.mobiautorevenda.dto.login.request.SetPasswordRequest;
import src.com.mobiautorevenda.dto.login.response.UserLoginResponse;
import src.com.mobiautorevenda.service.LoginService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/login")
@Tag(name = "Login", description = "Login Management")
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "Login to the system using email and password to generate the access token")
    @PostMapping
    public UserLoginResponse login(@RequestBody LoginRequest loginRequest) {
        log.info("Login - User: {}", loginRequest.getEmail());
        return loginService.login(loginRequest);
    }

    @Operation(summary = "Set access password")
    @PostMapping("/set-password")
    public void setPassword(@RequestBody @Valid SetPasswordRequest setPasswordRequest) {
        loginService.setPassword(setPasswordRequest);
    }
}
