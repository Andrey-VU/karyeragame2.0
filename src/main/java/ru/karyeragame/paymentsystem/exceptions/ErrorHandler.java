package ru.karyeragame.paymentsystem.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {


    //400 Bad Request
    @Hidden
    @ExceptionHandler({
            InvalidFormatException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            InvalidDataException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final Exception e) {
        return handleErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    //402 Payment Required
    @Hidden
    @ExceptionHandler({
            NotEnoughMoneyPaymentRequiredException.class
    })
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorResponse handlePaymentRequiredException(final Exception e) {
        return handleErrorResponse(HttpStatus.PAYMENT_REQUIRED, e);
    }

    //403 Forbidden
    @Hidden
    @ExceptionHandler({
            NotEnoughRightsException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final Exception e) {
        return handleErrorResponse(HttpStatus.FORBIDDEN, e);
    }

    //404 Not Found
    @Hidden
    @ExceptionHandler({
            NotFoundException.class,
            EntityNotFoundException.class,
            BankAccountNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final Exception e) {
        return handleErrorResponse(HttpStatus.NOT_FOUND, e);
    }

    //409 Conflict
    @Hidden
    @ExceptionHandler({
            DataConflictException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final Exception e) {
        return handleErrorResponse(HttpStatus.CONFLICT, e);
    }

    //500 Internal server error
    @Hidden
    @ExceptionHandler({
            LoadDataException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(final Exception e) {
        return handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ErrorResponse handleErrorResponse(HttpStatus status, Exception e) {
        log.error("Стек трейс ошибки: {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(status, e.getMessage(), LocalDateTime.now());
    }
}
