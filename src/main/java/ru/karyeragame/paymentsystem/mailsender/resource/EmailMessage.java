package ru.karyeragame.paymentsystem.mailsender.resource;

/*
 * Тестовый класс для проверки emailSender
 */

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailMessage(
        @Email
        @NotBlank
        String to,
        String subject,
        String message,
        @NotBlank
        String path) {
}
