package ru.karyeragame.paymentsystem.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.karyeragame.paymentsystem.exceptions.ErrorPasswordRecovery;
import ru.karyeragame.paymentsystem.exceptions.NotFoundException;
import ru.karyeragame.paymentsystem.mailsender.service.EmailService;
import ru.karyeragame.paymentsystem.security.recoverPassword.model.PasswordResetToken;
import ru.karyeragame.paymentsystem.security.recoverPassword.repository.PasswordTokenRepository;
import ru.karyeragame.paymentsystem.user.model.User;
import ru.karyeragame.paymentsystem.user.service.UserService;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService{
    PasswordTokenRepository passwordTokenRepository;
    UserService userService;
    EmailService emailService;
    String contextPath;

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
            : isTokenExpired(passToken) ? "expired"
            : null;
    }

    @Override
    public void resetPassword(String userEmail) {
        try {
            log.info("Find user by email {} started", userEmail);
            User user = userService.findUserByEmail(userEmail);
            // создаём токен
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = userService.createPasswordResetTokenForUser(user, token);
            log.info("User Id {} created token for reset password", user.getId());

            String linkForResetPassword = contextPath + "\"/user/changePassword?token=\"" + token;

            emailService.sendSimpleMessage(userEmail, "Восстановление пароля в платежной системе Игры «КарьерА»",
                "Это письмо платежной системы Игры «КарьерА». " +
                    "Вы запрашивали восстановление пароля. Пройдите по ссылке, чтобы сбросить текущий пароль и " +
                    "задать новый: " +
                    linkForResetPassword +
                    "\n" +
                    "Это письмо отправлено автоматически, на него не нужно отвечать.");

        } catch (NotFoundException e) {
            log.warn("e-mail {} is not found", userEmail);
            throw new ErrorPasswordRecovery(e.getMessage());  // нужно ли тут бросать исключение ???
        }
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;  //
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return passToken.getExpiryDate().toInstant().isBefore(Instant.now());
    }


}
