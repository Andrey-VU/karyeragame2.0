package ru.karyeragame.paymentsystem.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.karyeragame.paymentsystem.avatar.model.Avatar;
import ru.karyeragame.paymentsystem.avatar.repository.AvatarRepository;
import ru.karyeragame.paymentsystem.enums.Roles;
import ru.karyeragame.paymentsystem.exceptions.NotFoundException;
import ru.karyeragame.paymentsystem.security.recoverPassword.model.PasswordResetToken;
import ru.karyeragame.paymentsystem.security.recoverPassword.repository.PasswordTokenRepository;
import ru.karyeragame.paymentsystem.user.dto.NewUserDto;
import ru.karyeragame.paymentsystem.user.dto.UserDto;
import ru.karyeragame.paymentsystem.user.mapper.UserMapper;
import ru.karyeragame.paymentsystem.user.model.User;
import ru.karyeragame.paymentsystem.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final AvatarRepository avatarRepository;
    private final PasswordTokenRepository passwordTokenRepository;

    @Override
    public PasswordResetToken createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        log.info("Object {} is created", myToken);
        myToken = passwordTokenRepository.save(myToken);
        log.info("Object {} is saved to repo", myToken);
        return myToken;
    }

    @Override
    public User findUserByEmail(String userEmail) {
        return repository.findByEmail(userEmail).orElseThrow(()
            -> new NotFoundException(String.format("User with email {} is not found", userEmail)));
    }

    @Override
    public UserDto changeUserPassword(Long userId, String newPassword) {
        User user = getUserEntity(userId);
        user.setPassword(encoder.encode(newPassword));
        return mapper.toDto(repository.save(user));
    }

    @Override
    @Transactional
    public UserDto register(NewUserDto dto) {
        User user = mapper.toEntity(dto);
        user.setRole(Roles.USER);
        user.setPassword(encoder.encode(user.getPassword()));
        return mapper.toDto(repository.save(user));
    }

    private Avatar getAvatar(Long id) {
        return avatarRepository.findById(id).orElseThrow(() -> new NotFoundException("Avatar not found with id %d", id));
    }

    @Override
    public UserDto getUser(Long id) {
        return mapper.toDto(getUserEntity(id));
    }

    @Override
    public List<UserDto> getAllUsers(int size, int from) {
        Pageable pageable = PageRequest.of(from, size);
        PagedListHolder<UserDto> page = new PagedListHolder<>(repository.findAll(pageable)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        return page.getPageList();
    }

    @Override
    @Transactional
    public UserDto makeUserAdmin(Long id) {
        User user = getUserEntity(id);
        user.setRole(Roles.ADMIN);
        return mapper.toDto(repository.save(user));
    }

    private User getUserEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
