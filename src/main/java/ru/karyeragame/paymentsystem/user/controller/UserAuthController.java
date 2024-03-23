package ru.karyeragame.paymentsystem.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.exceptions.ErrorResponse;
import ru.karyeragame.paymentsystem.security.AuthResponse;
import ru.karyeragame.paymentsystem.user.dto.AuthUserDto;
import ru.karyeragame.paymentsystem.user.dto.NewUserDto;
import ru.karyeragame.paymentsystem.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserAuthController {
    private final UserService service;

    @PostMapping("/auth/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "User registration", description = "Регистрация пользователя в приложении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"CONFLICT\", " +
                                    "\"message\": \"User with this email already exists\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}")))
    })
    public AuthResponse signUp(@Valid @RequestBody NewUserDto dto,
                               HttpServletResponse response) {
        log.info("signUp started with body: {}", dto);
        AuthResponse result = service.signUp(dto, response);
        log.info("signUp finished with result: {}", result);
        return result;
    }

    @PostMapping("/auth/sign-in")
    @Operation(summary = "User authentication", description = "Аутентификация пользователя в приложении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}")))
    })
    public AuthResponse signIn(@Valid @RequestBody AuthUserDto dto,
                               HttpServletResponse response) {
        log.info("signIn started with dto: {}", dto);
        AuthResponse result = service.signIn(dto, response);
        log.info("signIn finished with result: {}", result);
        return result;
    }

    @PostMapping("/auth/refresh-token")
    @Operation(summary = "Refresh token", description = "Запрос на обновление токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"BAD_REQUEST\", " +
                                    "\"message\": \"Invalid token format\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:29\"}")))
    })
    public AuthResponse refreshToken(HttpServletRequest request,
                                     HttpServletResponse response) {
        log.info("refreshToken started");
        AuthResponse result = service.refreshToken(request, response);
        log.info("refreshToken finished with result: {}", result);
        return result;
    }
}
