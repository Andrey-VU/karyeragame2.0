package ru.karyeragame.paymentsystem.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthUserDto {

    @Schema(description = "User's email", example = "bigbiggerbiggest@gmail.com")
    @Email(regexp = "^(?=.{1,255}@)[A-Za-z0-9_-]" +
            "+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]" +
            "+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank
    @Size(min = 6, max = 255)
    private String email;

    @Schema(description = "User's password", example = "kAk01toNa60r51mVol%v")
    @Pattern(regexp = "^[^\\s][\\p{ASCII}\\p{Punct}\\p{Graph}]+$")// латиница, 0-9, видимые символы
    @NotBlank
    @Size(min = 5, max = 100)
    private String password;
}
