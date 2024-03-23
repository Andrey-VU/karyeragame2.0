package ru.karyeragame.paymentsystem.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "Http status", example = "404 NOT_FOUND")
    HttpStatus status;
    @Schema(description = "Exception's message", example = "User was not found")
    String message;
    @Schema(description = "Exception's timestamp", example = "2024-03-17 18:01:23")
    LocalDateTime timestamp;
}
