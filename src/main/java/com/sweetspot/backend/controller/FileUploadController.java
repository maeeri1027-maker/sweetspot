package com.sweetspot.backend.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    // 長辺がこのサイズを超える場合は自動的に縮小してから保存する
    private static final int MAX_DIMENSION = 1280;

    private final Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

    public FileUploadController() throws IOException {
        Files.createDirectories(uploadDir);
    }

    @PostMapping
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("ファイルが空です");
        }

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "photo");
        String ext = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0) {
            ext = originalName.substring(dotIndex + 1).toLowerCase();
        }

        BufferedImage original = ImageIO.read(file.getInputStream());

        String formatName = ext.isBlank() ? "jpg" : ext;
        if (!ImageIO.getImageWritersByFormatName(formatName).hasNext()) {
            formatName = "jpg"; // 対応していない拡張子はjpgとして保存
        }

        String savedName = UUID.randomUUID() + "." + formatName;
        Path target = uploadDir.resolve(savedName);

        if (original != null) {
            boolean supportsAlpha = formatName.equals("png") || formatName.equals("gif");
            int imageType = supportsAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            BufferedImage resized = resizeAndConvert(original, imageType);
            ImageIO.write(resized, formatName, target.toFile());
        } else {
            // 画像として読み込めない形式（HEICなど）はリサイズせずそのまま保存
            Files.copy(file.getInputStream(), target);
        }

        String url = "http://localhost:8080/uploads/" + savedName;
        return Map.of("url", url);
    }

    // 長辺がMAX_DIMENSIONを超えていれば縮小し、アスペクト比は維持したまま返す
    private BufferedImage resizeAndConvert(BufferedImage original, int imageType) {
        int width = original.getWidth();
        int height = original.getHeight();
        int maxSide = Math.max(width, height);

        int targetWidth = width;
        int targetHeight = height;
        if (maxSide > MAX_DIMENSION) {
            double scale = (double) MAX_DIMENSION / maxSide;
            targetWidth = Math.max(1, (int) Math.round(width * scale));
            targetHeight = Math.max(1, (int) Math.round(height * scale));
        }

        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, imageType);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (imageType == BufferedImage.TYPE_INT_RGB) {
            // JPEGなど透過非対応の形式は白背景で塗ってから描画
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, targetWidth, targetHeight);
        }
        g.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resized;
    }
}
