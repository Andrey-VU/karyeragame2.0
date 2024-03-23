package ru.karyeragame.paymentsystem.avatar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.karyeragame.paymentsystem.avatar.dto.AvatarDto;
import ru.karyeragame.paymentsystem.avatar.service.AvatarService;
import ru.karyeragame.paymentsystem.exceptions.ErrorResponse;
import ru.karyeragame.paymentsystem.exceptions.LoadDataException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/avatars")
@RequiredArgsConstructor
@Slf4j
public class AvatarController {

    @Autowired
    private final AvatarService service;

    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Avatar creation", description = "Эндпойнт для создания аватара пользователя. " +
            "На входе MultipartFile file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AvatarDto.class))}),
            @ApiResponse(responseCode = "400", description = "Avatar has to be an image",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"BAD_REQUEST\", " +
                                    "\"message\": \"Avatar has to be an image\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}"))),
            @ApiResponse(responseCode = "500", description = "Cannot save image: fileName",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"INTERNAL_SERVER_ERROR\", " +
                                    "\"message\": \"Cannot save image: fileName\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}")))
    })
    public AvatarDto saveAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable(name = "userId") Long userId) throws IOException {
        log.info("saveAvatar started with file: {} and userId: {}", file.getOriginalFilename(), userId);
        AvatarDto result = service.saveAvatar(file, userId);
        log.info("saveAvatar finished with result: {}", result);
        return result;
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user info",
            description = "Эндпойнт вызывает сервис для загрузки аватара пользователя по его идентификатору. " +
                    "Полученный файл изображения открывается как InputStream и отправляется в ответ на запрос " +
                    "в формате image/png. Если файл не удалось загрузить, возникает исключение.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully loaded the user's avatar",
                    content = {@Content(mediaType = MediaType.IMAGE_PNG_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Avatar with user id 2 not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"Avatar with user id 2 not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}"))),
            @ApiResponse(responseCode = "500", description = "Cannot save image: fileName",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"INTERNAL_SERVER_ERROR\", " +
                                    "\"message\": \"Cannot load image\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:25\"}")))
    })
    public void loadAvatar(@PathVariable("userId") Long userId, HttpServletResponse response) throws IOException {
        log.info("getAvatar started with id: {}", userId);
        File image = service.loadAvatar(userId);

        try (InputStream is = new FileInputStream(image)) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            throw new LoadDataException("Cannot load image");
        }

        log.info("getAvatar finished");
    }
}