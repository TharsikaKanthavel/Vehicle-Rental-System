// src/main/java/com/vehiclerental/controllers/FileServeController.java
package com.vehiclerental.controllers;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;

@Controller
public class FileServeController {

    private static final Path ROOT = Paths.get("uploads/inspections");

    @GetMapping("/files/inspections/{deliveryId}/{angle}")
    public ResponseEntity<Resource> serve(
            @PathVariable Long deliveryId,
            @PathVariable String angle
    ) {
        try {
            Path dir = ROOT.resolve(String.valueOf(deliveryId));
            Path jpg = dir.resolve(angle + ".jpg");
            Path png = dir.resolve(angle + ".png");
            Path chosen = Files.exists(jpg) ? jpg : Files.exists(png) ? png : null;
            if (chosen == null) return ResponseEntity.notFound().build();

            MediaType type = chosen.toString().endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
            return ResponseEntity.ok()
                    .contentType(type)
                    .cacheControl(CacheControl.noCache())
                    .body(new FileSystemResource(chosen.toFile()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
