package ru.karyeragame.paymentsystem.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class NewPaymentDto {

    @Schema(description = "Payment amount", example = "4567.89")
    @NotNull
    @Digits(integer = 10, fraction = 2) //10 знаков до точки и 2 знака после запятой
    private BigDecimal amount;

    @Schema(description = "Payer id", example = "2")
    @NotNull
    private Long accountIdFrom;

    @Schema(description = "Recipient id", example = "3")
    @NotNull
    private Long accountIdTo;

    @Schema(description = "Purpose of payment", example = "Налог на доходы физических лиц")
    @NotNull
    @Size(min = 5, max = 200, message = "Назначение платежа не может быть меньше 5 символов и не больше 200")
    private String message;

    @Schema(description = "Game id", example = "5")
    @NotNull
    private Long gameId;
}
