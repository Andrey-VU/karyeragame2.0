package ru.karyeragame.paymentsystem.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.karyeragame.paymentsystem.game.model.GameStatus;

@Builder
@Data
public class UpdateGameDto {

    @Schema(description = "Name of the game", example = "Поглощение и крах")
    private String name;

    @Schema(description = "Name of the game", example = "Захватывающая игра, где вы вступите в схватку за господство " +
            "и выживание. В этой стратегической битве вы пройдете через эпические сражения, " +
            "чтобы установить свое владычество или падение вашего врага")
    private String comment;

    @Schema(description = "Game status", example = "WAITING")
    private GameStatus status;

    @Schema(description = "Starting balance of game participants", example = "200")
    private Float startBalance;
}