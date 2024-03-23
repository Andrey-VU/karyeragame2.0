package ru.karyeragame.paymentsystem.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.karyeragame.paymentsystem.game.model.GameStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class GameDto {

    @Schema(description = "Game id", example = "3")
    private Long id;

    @Schema(description = "Name of the game",
            example = "Влияние качества советских межпланетных кораблей на становление личности Дарта Вейдера.")
    private String name;

    @Schema(description = "Game commentary",
            example = "Эта игра исследует, как опыт работы на советских межпланетных кораблях сформировал личность " +
                    "Дарта Вейдера, и как его путешествия в космосе повлияли на его характер и мировоззрение.")
    private String comment;

    @Schema(description = "Creation date-time", example = "2024-03-17 18:01:23")
    private LocalDateTime createdOn;

    @Schema(description = "ID of the participant who created the game", example = "2")
    private Long createdById;

    @Schema(description = "Game status", example = "ACTIVE")
    private GameStatus status;

    @Schema(description = "Starting balance of game participants", example = "100.0")
    private Float startBalance;

    @Schema(description = "List of IDs of participants in this game", example = "[123, 456, 789]")
    private Set<Long> participantsIds;
}