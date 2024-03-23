package ru.karyeragame.paymentsystem.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @Schema(description = "Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWdiaWdnZXJiaWdnZXN0QGdtYWlsLmNvbSIsImlhdCI6MTcxMTE4NTQ2OSwiZXhwIjoxNzExMjcxODY5fQ.gqNMmUFwWgBoa5eT6LrvjPuWleazxIg5wvDS5LO3C7g")
    private String token;

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWdiaWdnZXJiaWdnZXN0QGdtYWlsLmNvbSIsImlhdCI6MTcxMTE4NTQ2OSwiZXhwIjoxNzExNzkwMjY5fQ.rJwPVmB7vXsvXvxEqc1FgPTHEyFz6imRlf9ECjxC3Oc")
    private String refreshToken;
}
