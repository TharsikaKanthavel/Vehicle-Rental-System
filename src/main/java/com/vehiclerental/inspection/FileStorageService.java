package com.vehiclerental.inspection;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads/inspections"); // relative to app working dir

    public String save(MultipartFile file, Long deliveryId, String angle) throws IOException {
        if (file == null || file.isEmpty()) throw new IOException("Missing file: " + angle);
        String ct = file.getContentType();
        if (ct == null || !(ct.equalsIgnoreCase("image/jpeg") || ct.equalsIgnoreCase("image/png")))
            throw new IOException("Invalid file type for "+angle+" (only JPG/PNG).");
        if (file.getSize() > 5 * 1024 * 1024)
            throw new IOException("File too large for "+angle+" (max 5MB).");

        Path dir = root.resolve(String.valueOf(deliveryId));
        Files.createDirectories(dir);
        String ext = ct.equalsIgnoreCase("image/png") ? ".png" : ".jpg";
        Path target = dir.resolve(angle + ext);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString().replace('\\','/'); // store portable path
    }
}
