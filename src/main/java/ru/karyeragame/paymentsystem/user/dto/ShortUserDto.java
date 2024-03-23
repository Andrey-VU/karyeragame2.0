package ru.karyeragame.paymentsystem.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.karyeragame.paymentsystem.user.model.ProfileStatus;

@Builder
@Data
public class ShortUserDto {

    @Schema(description = "User id", example = "3")
    private Long id;

    @Schema(description = "User's name or login", example = "Виктор Семёнович")
    private String name;

    @Schema(description = "User's email", example = "victory@mail.ru")
    private String email;

    @Schema(description = "Account status ", example = "WAITING")
    private ProfileStatus status;
}
