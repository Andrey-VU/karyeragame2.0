package ru.karyeragame.paymentsystem.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.user.dto.NewUserDto;
import ru.karyeragame.paymentsystem.user.dto.UserDto;
import ru.karyeragame.paymentsystem.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    @Autowired
    private final UserService service;

    @Operation(summary = "Registration endpoint")
    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(
            @Parameter(description = "User registration")
            @Valid @RequestBody NewUserDto dto) {
        log.info("registerUser started with body: {}", dto);
        UserDto result = service.register(dto);
        log.info("registerUser finished with result: {}", result);
        return result;
    }

    @Operation(summary = "Get a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))})
    })
    @GetMapping("/{id}")
    public UserDto getUser(@Parameter(description = "id of user to be searched")
                           @PathVariable(name = "id") Long id) {
        log.info("getUser started with id: {}", id);
        UserDto result = service.getUser(id);
        log.info("getUser finished with result: {}", result);
        return result;
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserDto> getAllUsers(@Parameter(description = "Find all users")
                                     @RequestParam(value = "size", defaultValue = "10", required = false) @Min(10) int size,
                                     @RequestParam(value = "from", defaultValue = "1", required = false) @Min(1) int from) {
        log.info("getAllUsers started with params: " +
                "size: {}; " +
                "from: {}", size, from);
        List<UserDto> result = service.getAllUsers(size, from - 1);
        log.info("getAllUsers finished with result: {}", result);
        return result;
    }

    @PatchMapping("/admin/{id}")
    public UserDto makeUserAdmin(@PathVariable(name = "id") Long id) {
        log.info("makeUserAdmin started with id: {}", id);
        UserDto result = service.makeUserAdmin(id);
        log.info("makeUserAdmin finished with result: {}", result);
        return result;
    }

    @DeleteMapping("/admin/{id}")
    public void deleteUserByAdmin(@PathVariable(name = "id") Long id) {
        log.info("deleteUserByAdmin started with id: {}", id);
        service.deleteUserByAdmin(id);
        log.info("deleteUserByAdmin has finished");
    }
}
