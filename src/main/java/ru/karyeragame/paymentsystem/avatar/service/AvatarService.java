package ru.karyeragame.paymentsystem.avatar.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.karyeragame.paymentsystem.avatar.dto.AvatarDto;
import ru.karyeragame.paymentsystem.avatar.mapper.AvatarMapper;
import ru.karyeragame.paymentsystem.avatar.model.Avatar;
import ru.karyeragame.paymentsystem.avatar.repository.AvatarRepository;
import ru.karyeragame.paymentsystem.exceptions.InvalidFormatException;
import ru.karyeragame.paymentsystem.exceptions.NotFoundException;
import ru.karyeragame.paymentsystem.user.service.UserService;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final AvatarMapper mapper;
    private final AvatarRepository repository;
    private final UserService userService;

    @Transactional
    public AvatarDto saveAvatar(MultipartFile file, Long userId) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        Optional<Avatar> avatar = repository.findByUserId(userId);
        Avatar result;
        if (avatar.isPresent()) {
            avatar.get().setUrl(userId + "/" + fileName);
            avatar.get().setUserId(userId);
            result = repository.save(avatar.get());
        } else {
            Avatar entity = new Avatar();
            entity.setUrl(userId + "/" + fileName);
            entity.setUserId(userId);
            result = repository.save(entity);
        }
        userService.updateUserAvatar(result, userId);

        if (ImageIO.read(file.getInputStream()) == null) {
            throw new InvalidFormatException("Avatar has to be an image");
        }
        String uploadDir = Paths.get(".", "data", "avatars", userId.toString()).toString();

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        } else {
            FileUtils.cleanDirectory(new File(uploadDir));
        }
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Cannot save image" + fileName);
        }
        return mapper.toDto(result);
    }

    public void getAvatar(Long userId, HttpServletResponse response) throws IOException {
        AvatarDto avatarDto = mapper.toDto(getAvatarEntityByUserId(userId));

        Path uploadPath = Paths.get("./data/avatars/" + avatarDto.getUrl());

        if (!Files.exists(uploadPath)) {
            throw new NotFoundException("Avatar with id %d not found", userId);
        }
        try (InputStream is = new FileInputStream(uploadPath.toFile())) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            throw new IOException("Cannot return image");
        }
    }

    public Avatar getAvatarEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Avatar not found with id %d", id));
    }

    public Avatar getAvatarEntityByUserId(Long userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Avatar not found with user id: %d", userId));
    }
}
