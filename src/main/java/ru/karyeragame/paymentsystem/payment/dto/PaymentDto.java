package ru.karyeragame.paymentsystem.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder
@Data
public class PaymentDto {

    @Schema(description = "Payment id", example = "3")
    private Long id;

    @Schema(description = "Payment amount", example = "4567.89")
    private BigDecimal amount;

    @Schema(description = "Creation date-time", example = "2024-03-17 18:01:23")
    private LocalDateTime paymentOn;

    @Schema(description = "Payer id", example = "2")
    private Long accountIdFrom;

    @Schema(description = "Recipient id", example = "3")
    private Long accountIdTo;

    @Schema(description = "Purpose of payment", example = "Налог на доходы физических лиц")
    private String message;

    @Schema(description = "Game id", example = "17")
    private Long gameId;
}
