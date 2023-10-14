package ru.karyeragame.paymentsystem.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final Exception e) {
        log.error("Стек трейс ошибки: {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(HttpStatus.NOT_FOUND,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({InvalidFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final Exception e) {
        log.error("Стек трейс ошибки: {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({NotEnoughRightsException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final Exception e) {
        log.error("Стек трейс ошибки: {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(HttpStatus.FORBIDDEN,
                e.getMessage(),
                LocalDateTime.now());
    }
}
