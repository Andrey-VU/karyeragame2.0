
package ru.karyeragame.paymentsystem.avatar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AvatarDto {

    @Schema(description = "Avatar id", example = "3")
    private Long id;

    @Schema(description = "avatar pictures url", example = "https://example.com/avatar1.jpg")
    private String url;

    @Schema(description = "ID of the user to whom this avatar was added", example = "5")
    private Long userId;
}
