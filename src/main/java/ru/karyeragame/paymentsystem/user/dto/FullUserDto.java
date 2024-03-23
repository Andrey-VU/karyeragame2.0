package ru.karyeragame.paymentsystem.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.karyeragame.paymentsystem.user.model.ProfileStatus;
import ru.karyeragame.paymentsystem.user.model.Roles;

import java.time.LocalDateTime;

@Builder
@Data
public class FullUserDto {

    @Schema(description = "User id", example = "3")
    private Long id;

    @Schema(description = "User's name or login", example = "Виктор Семёнович")
    private String name;

    @Schema(description = "User's email", example = "victory@mail.ru")
    private String email;

    @Schema(description = "User's avatar id", example = "17")
    private Long avatarId;

    @Schema(description = "Creation date-time", example = "2024-03-17 18:01:23")
    private LocalDateTime createdOn;

    @Schema(description = "User's role in game", example = "USER")
    private Roles role;

    @Schema(description = "Account status ", example = "WAITING")
    private ProfileStatus status;

    @Schema(description = "Creation date-time", example = "2024-03-17 18:01:24")
    private LocalDateTime removedOn;

    @Schema(description = "ID of the user who performed the deletion", example = "2")
    private Long removedBy;
}
