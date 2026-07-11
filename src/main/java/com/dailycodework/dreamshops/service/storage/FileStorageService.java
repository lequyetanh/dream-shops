package com.dailycodework.dreamshops.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final String URL_PREFIX = "/uploads/";
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String store(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File ảnh không được để trống");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Chỉ chấp nhận file ảnh (jpeg, png, gif, webp)");
        }

        String original = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : ""
        );
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
        String filename = UUID.randomUUID() + ext;

        Path targetDir = Paths.get(uploadDir, subDir).toAbsolutePath().normalize();
        Files.createDirectories(targetDir);
        Path targetPath = targetDir.resolve(filename).normalize();
        if (!targetPath.startsWith(targetDir)) {
            throw new IllegalArgumentException("Tên file không hợp lệ");
        }
        file.transferTo(targetPath);

        return URL_PREFIX + subDir + "/" + filename;
    }

    public void delete(String relativeUrl) {
        if (relativeUrl == null || !relativeUrl.startsWith(URL_PREFIX)) return;
        try {
            Path path = Paths.get(uploadDir, relativeUrl.substring(URL_PREFIX.length())).toAbsolutePath().normalize();
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
