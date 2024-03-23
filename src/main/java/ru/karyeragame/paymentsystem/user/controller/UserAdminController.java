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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.exceptions.ErrorResponse;
import ru.karyeragame.paymentsystem.user.dto.FullUserDto;
import ru.karyeragame.paymentsystem.user.dto.ShortUserDto;
import ru.karyeragame.paymentsystem.user.model.ProfileStatus;
import ru.karyeragame.paymentsystem.user.model.Roles;
import ru.karyeragame.paymentsystem.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users/admin")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserService service;

    @PatchMapping("/archive/{id}/recover")
    @Operation(summary = "Recover user from archive by id", description = "Восстановление активного статуса для пользователя по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active status restored",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User was not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"CONFLICT\", " +
                                    "\"message\": \"User with id 7 is not in archive\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:27\"}")))
    })
    public FullUserDto recoverUser(@PathVariable(name = "id") @Positive Long id) {
        log.info("recoverUser started with id: {}", id);
        FullUserDto result = service.recoverUser(id);
        log.info("recoverUser finished with result: {}", result);
        return result;
    }

    @DeleteMapping("/archive/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Archiving a user by id", description = "Перенос учетной записи пользователя в архив")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user has been archived"),
            @ApiResponse(responseCode = "403", description = "There are not enough rights to delete user with role: ADMIN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"FORBIDDEN\", " +
                                    "\"message\": \"There are not enough rights to delete user with role: ADMIN\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}"))),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User was not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}"))),
            @ApiResponse(responseCode = "409", description = "User has already been added to the archive",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"CONFLICT\", " +
                                    "\"message\": \"User has already been added to the archive\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:27\"}")))
    })
    public void archiveUser(@PathVariable(name = "id") @Positive Long id) {
        log.info("deleteUserByAdmin started with id: {}", id);
        service.archiveUser(id);
        log.info("deleteUserByAdmin has finished");
    }

    @GetMapping("/archive")
    @Operation(summary = "Get all archived users", description = "Запрос на вывод всех архивированных пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User list provided",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullUserDto.class))})
    })
    public List<ShortUserDto> findAllArchivedUsers(@Parameter(description = "The current page size")
                                                   @RequestParam(value = "size", defaultValue = "10", required = false) @Min(10) int size,
                                                   @Parameter(description = "The current page")
                                                   @RequestParam(value = "from", defaultValue = "0", required = false) @Min(0) int from) {
        log.info("findAllArchivedUsers started with size: {}, from: {}", size, from);
        List<ShortUserDto> result = service.findAllArchivedUsers(size, from);
        log.info("findAllArchivedUsers finished with result: {}", result);
        return result;
    }

    @GetMapping("/archive/{id}")
    @Operation(summary = "Get archived user by id", description = "Запрос на вывод архивированного пользователя по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User with id 3 was not found in the archive",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User with id 3 was not found in the archive\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}")))
    })
    public FullUserDto findArchivedUser(@PathVariable(name = "id") @Positive Long id) {
        log.info("findArchivedUser started with id: {}", id);
        FullUserDto result = service.findArchivedUserById(id);
        log.info("findArchivedUser finished with result: {}", result);
        return result;
    }

    @PatchMapping("/{id}/role")
    @Operation(summary = "Change user role", description = "Запрос на изменение роли пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's role has been changed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User was not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}"))),
            @ApiResponse(responseCode = "409", description = "Archived user cannot be made an admin",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"CONFLICT\", " +
                                    "\"message\": \"Archived user cannot be made an admin\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:27\"}")))
    })
    public FullUserDto changeUserRole(@Parameter(description = "New Role for user by id")
                                      @RequestParam(name = "role") Roles role, @PathVariable(name = "id") @Positive Long id) {
        log.info("changeUserRole started with id: {}", id);
        FullUserDto result = service.changeUserRole(role, id);
        log.info("changeUserRole finished with result: {}", result);
        return result;
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change user status", description = "Запрос на изменение статуса пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's status has been changed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "You have to use delete operation to set ARCHIVE status",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"FORBIDDEN\", " +
                                    "\"message\": \"You have to use delete operation to set ARCHIVE status\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}"))),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User was not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}")))
    })
    public FullUserDto changeUserStatus(@RequestParam(name = "status") ProfileStatus status,
                                        @PathVariable(name = "id") @Positive Long id) {
        log.info("changeUserStatus started with status: {} and id: {}", status, id);
        FullUserDto result = service.changeUserStatus(status, id);
        log.info("changeUserStatus finished with result: {}", result);
        return result;
    }
}
