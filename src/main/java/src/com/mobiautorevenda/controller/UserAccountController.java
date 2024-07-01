package src.com.mobiautorevenda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.com.mobiautorevenda.dto.user.UserAccountDto;
import src.com.mobiautorevenda.dto.user.request.CreateUserAccountRequest;
import src.com.mobiautorevenda.dto.user.response.UserAccountDataResponse;
import src.com.mobiautorevenda.service.UserAccountService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users", description = "User management")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Operation(summary = "Create a new user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid CreateUserAccountRequest dataUserAccountRequest, Principal loggedInUser) {
        userAccountService.createNewUserAccount(dataUserAccountRequest, loggedInUser);
    }

    @Operation(summary = "List all registered users and their associated data")
    @GetMapping()
    public List<UserAccountDataResponse> findAll() {
        return userAccountService.findAllUsers();
    }

    @Operation(summary = "Update user data by their ID")
    @PutMapping("/{userId}")
    public void updateUser(@PathVariable String userId, @RequestBody @Valid UserAccountDto userAccountData) {
        userAccountService.updateUserAccountDataById(userId, userAccountData);
    }

    @Operation(summary = "Deletes user by ID")
    @DeleteMapping("/{userId}")
    public void update(@PathVariable String userId) {
        userAccountService.deleteUserAccountById(userId);
    }

}
