package ru.karyeragame.paymentsystem.bank_account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.bank_account.model.BankAccountType;
import ru.karyeragame.paymentsystem.bank_account.service.BankAccountService;
import ru.karyeragame.paymentsystem.exceptions.ErrorResponse;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/bank-accounts")
@RequiredArgsConstructor
@Slf4j
public class BankAccountController {

    @Autowired
    private final BankAccountService service;

    @GetMapping("/{userId}/balance")
    @Operation(summary = "Get user balance",
            description = "При положительном статусе в ответе мы получаем только цифровое значение баланса (BigDecimal)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Object was not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"status\": \"NOT_FOUND\", " +
                                    "\"message\": \"Object was not found\", " +
                                    "\"timestamp\": \"2024-03-17 18:01:23\"}")))
    })
    public BigDecimal getBalanceByUserId(@PathVariable Long userId,
                                         @RequestParam(name = "type") BankAccountType type) {
        log.info("Входящий запрос GET /bank-accounts/{}/balance?type={}", userId, type);
        BigDecimal balance = service.getBalanceByUserId(userId, type);
        log.info("Исходящий ответ: {}", balance);
        return balance;
    }
}
