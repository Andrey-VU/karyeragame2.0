package ru.karyeragame.paymentsystem.security.recoverPassword.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.karyeragame.paymentsystem.security.recoverPassword.dto.PasswordDtoFinal;
import ru.karyeragame.paymentsystem.security.recoverPassword.dto.PasswordDtoRequest;
import ru.karyeragame.paymentsystem.security.recoverPassword.service.SecurityService;

@RestController
@RequestMapping("password/")
@Slf4j
@AllArgsConstructor
public class recoverController {
    SecurityService securityService;

    /**
     * @param userEmail - введённый пользователем e-mail для восстановления пароля
     */
    @PostMapping("reset")
    public void resetPassword(@RequestParam("userEmail") String userEmail) {  // не забыть валидацию
        securityService.resetPassword(userEmail);
    }

    /**
     * @param token - переходя по ссылке для восстановления пароля пользователь передаёт токен из этой ссылки
     * @return - если токен корректный, открывается страница для введения нового пароля
     * Как эта страница должна открываться - видимо вопрос к фронту
     */
    @GetMapping("change")
    public String showChangePasswordPage(@RequestParam("token") String token) {
        return securityService.showChangePasswordPage(token);
    }

    @PostMapping("save")
    public void savePassword(@Valid @RequestBody PasswordDtoRequest dto){
        PasswordDtoFinal result = securityService.savePassword(dto);
    }
}