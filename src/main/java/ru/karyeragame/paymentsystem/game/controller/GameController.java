package ru.karyeragame.paymentsystem.game.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.exceptions.ErrorResponse;
import ru.karyeragame.paymentsystem.game.dto.GameDto;
import ru.karyeragame.paymentsystem.game.dto.NewGameDto;
import ru.karyeragame.paymentsystem.game.dto.UpdateGameDto;
import ru.karyeragame.paymentsystem.game.model.GameStatus;
import ru.karyeragame.paymentsystem.game.model.ParticipantsSort;
import ru.karyeragame.paymentsystem.game.service.GameService;
import ru.karyeragame.paymentsystem.user.dto.ShortUserDto;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Slf4j
@Validated
public class GameController {

    private final GameService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Game creation", description = "Эндпойнт для создания игры.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"User not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}")))
    })
    public GameDto addGame(@RequestBody @Valid NewGameDto dto, @RequestParam(name = "initiatorId") Long id) {
        log.info("addGame started with dto: {}", dto);
        GameDto result = service.addGame(dto, id);
        log.info("addGame finished with result: {}", result);
        return result;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get game by id", description = "Эндпойнт для получении данных об игре по ее id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))}),
            @ApiResponse(responseCode = "404", description = "Game not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"Game not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}")))
    })
    public GameDto getGame(@PathVariable("id") Long id) {
        log.info("getGame started with id: {}", id);
        GameDto result = service.getGame(id);
        log.info("getGame finished with result: {}", result);
        return result;
    }

    @GetMapping
    @Operation(summary = "Get a list of all games", description = "Запрос на получение всех игр из БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))})
    })
    public List<GameDto> getAllGames(@Parameter(description = "The current page size")
                                     @RequestParam(value = "size", defaultValue = "10", required = false) @Min(10) int size,
                                     @Parameter(description = "The current page")
                                     @RequestParam(value = "from", defaultValue = "0", required = false) @Min(0) int from) {
        log.info("getAllGames started with size: {} and from: {}", size, from);
        List<GameDto> result = service.getAllGames(size, from);
        log.info("getAllGames finished with result: {}", result);
        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove game by id", description = "Запрос на удаление игры из БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game deleted")
    })
    public void deleteGame(@PathVariable("id") Long id) {
        log.info("deleteGame started with id: {}", id);
        service.deleteGame(id);
        log.info("deleteGame finished");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update game by id", description = "Внесение изменений в сущность игра по её id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))})
    })
    public GameDto patchGame(@RequestBody UpdateGameDto dto, @PathVariable("id") Long id) {
        log.info("patchGame started with id: {} and upd: {}", id, dto);
        GameDto result = service.patchGame(dto, id);
        log.info("patchGame finished with result: {}", result);
        return result;
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update game status by id", description = "Изменение статуса игры по её id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game status updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))})
    })
    public GameDto changeGameStatus(@Parameter(description = "New status for game by id")
                                    @RequestParam(name = "status") GameStatus status,
                                    @PathVariable("id") Long id) {
        log.info("changeGameStatus started with status: {} and id: {}", status, id);
        GameDto result = service.changeGameStatus(status, id);
        log.info("changeGameStatus finished with result: {}", result);
        return result;
    }

    @PostMapping("/{gameId}/participants/{userId}")
    @Operation(summary = "Add the user into game", description = "Добавление игрока с userId в игру с gameId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participant has been added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))}),
            @ApiResponse(responseCode = "404", description = "Game not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"Game not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:27\"}")))
    })
    public GameDto addParticipant(@PathVariable(name = "gameId") Long gameId,
                                  @PathVariable(name = "userId") Long userId) {
        log.info("addParticipant started with gameId: {} and userId: {}", gameId, userId);
        GameDto result = service.addParticipant(gameId, userId);
        log.info("addParticipant finished with result: {}", result);
        return result;
    }

    @GetMapping("/{gameId}/participants")
//TODO: реализовать сортировку по имени, балансу (может еще по чему-либо) после создания аккаунтов
    @Operation(summary = "Get all participants by gameId", description = "Получение списка всех игроков игры по её id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of participants received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))})
    })
    public List<ShortUserDto> getAllParticipantByGame(@PathVariable(name = "gameId") Long gameId,
                                                      @Parameter(description = "Sort type for returned data")
                                                      @RequestParam(value = "sort", defaultValue = "USERNAME", required = false) ParticipantsSort sort,
                                                      @Parameter(description = "The current page size")
                                                      @RequestParam(value = "size", defaultValue = "10", required = false) @Min(10) int size,
                                                      @Parameter(description = "The current page")
                                                      @RequestParam(value = "from", defaultValue = "0", required = false) @Min(0) int from) {
        log.info("getAllParticipantByGame started with gameId: {}", gameId);
        List<ShortUserDto> result = service.getAllParticipantsByGame(gameId, size, from, sort);
        log.info("getAllParticipantByGame finished with result: {}", result);
        return result;
    }

    @DeleteMapping("/{gameId}/participants/{userId}")
    @Operation(summary = "Removing a participant from the game", description = "Удаление участника с userId из игры с gameId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participant has been removed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDto.class))}),
            @ApiResponse(responseCode = "404", description = "Game not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"Game not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:29\"}")))
    })
    public void deleteParticipant(@PathVariable(name = "gameId") Long gameId,
                                  @PathVariable(name = "userId") Long userId) {
        log.info("deleteParticipant started with gameId: {} and userId: {}", gameId, userId);
        service.deleteParticipant(gameId, userId);
        log.info("deleteParticipant finished");
    }
}
