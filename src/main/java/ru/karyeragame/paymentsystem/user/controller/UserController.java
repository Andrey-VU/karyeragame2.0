package ru.karyeragame.paymentsystem.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.exceptions.ErrorResponse;
import ru.karyeragame.paymentsystem.user.dto.FullUserDto;
import ru.karyeragame.paymentsystem.user.dto.ShortUserDto;
import ru.karyeragame.paymentsystem.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    @Operation(summary = "Get user info", description = "Получение информации по пользователю с указанным id. " +
            "При отсутствии пользователя в БД выбрасывается исключение.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User was not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}")))
    })
    public FullUserDto findUser(@PathVariable(name = "id") @Positive Long id) {
        log.info("findUser started with id: {}", id);
        FullUserDto result = service.findUserById(id);
        log.info("findUser finished with result: {}", result);
        return result;
    }

    @GetMapping
    @Operation(summary = "Get a list of all users", description = "Запрос на получение всех активных пользователей приложения")
    public List<ShortUserDto> findAllUsers(@Parameter(description = "The current page size")
                                           @RequestParam(value = "size", defaultValue = "10", required = false) @Min(10) int size,
                                           @Parameter(description = "The current page")
                                           @RequestParam(value = "from", defaultValue = "0", required = false) @Min(0) int from) {
        log.info("findAllUsers started with params: " +
                "size: {}; " +
                "from: {}", size, from);
        List<ShortUserDto> result = service.findAllUsers(size, from);
        log.info("findAllUsers finished with result: {}", result);
        return result;
    }
}
