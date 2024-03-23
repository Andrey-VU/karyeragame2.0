package ru.karyeragame.paymentsystem.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.exceptions.ErrorResponse;
import ru.karyeragame.paymentsystem.payment.dto.NewPaymentDto;
import ru.karyeragame.paymentsystem.payment.dto.PaymentDto;
import ru.karyeragame.paymentsystem.payment.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    @Autowired
    public final PaymentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create payment", description = "Создание платежа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))}),
            @ApiResponse(responseCode = "402", description = "There is not enough money in the account",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"PAYMENT_REQUIRED\", " +
                                    "\"message\": \"Not enough money\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}"))),
            @ApiResponse(responseCode = "404", description = "User / Game not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"Game not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}")))
    })
    public PaymentDto createPayment(@RequestBody NewPaymentDto dto) {
        log.info("Входящий запрос POST /payments: {}", dto);
        PaymentDto response = service.createPayment(dto);
        log.info("Исходящий ответ: {}", response);
        return response;
    }

    @GetMapping("/statement/account/{accountId}/game/{gameId}")
    @Operation(summary = "Get statement", description = "Получение выписки по счету пользователя с accountId в " +
            "игре с gameId. В выписке содержатся все операции по счету, выполненные в течение игры.")
    public List<PaymentDto> getStatement(
            @PathVariable Long accountId,
            @PathVariable Long gameId,
            @Parameter(description = "The current page")
            @RequestParam(defaultValue = "0") int from,
            @Parameter(description = "The current page size")
            @RequestParam(defaultValue = "10") int size) {
        log.info("Входящий запрос GET /payments/statement/account/{}/game/{}?from={}&size={}",
                accountId, gameId, from, size);
        List<PaymentDto> resultList = service.getStatementByAccountId(accountId, gameId, from, size);
        log.info("Исходящий ответ: {}", resultList);
        return resultList;
    }
}
