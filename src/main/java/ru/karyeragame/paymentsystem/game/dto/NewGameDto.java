package ru.karyeragame.paymentsystem.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewGameDto {

    @Schema(description = "Name of the game",
            example = "Влияние качества советских межпланетных кораблей на становление личности Дарта Вейдера")
    @NotBlank
    @Size(max = 100)
    private String name;

    @Schema(description = "Game commentary",
            example = "Эта игра исследует, как опыт работы на советских межпланетных кораблях сформировал личность " +
                    "Дарта Вейдера, и как его путешествия в космосе повлияли на его характер и мировоззрение.")
    @Size(max = 255)
    private String comment;

    @Schema(description = "ID of the participant who created the game", example = "2")
    @NotNull
    private Long createdBy;

    @Schema(description = "Starting balance of game participants", example = "100")
    @NotNull
    @PositiveOrZero
    private Float startBalance;
}