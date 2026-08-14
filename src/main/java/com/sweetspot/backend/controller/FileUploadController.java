package com.sweetspot.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    // DB容量の過度な圧迫を防ぐため、長辺最大800pxに自動リサイズ
    private static final int MAX_DIMENSION = 800;

    @PostMapping
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("ファイルが空です");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            contentType = "image/jpeg";
        }

        BufferedImage original = ImageIO.read(file.getInputStream());
        byte[] imageBytes;
        String mimeType = contentType;

        if (original != null) {
            // メモリ上での画像リサイズ処理
            boolean supportsAlpha = contentType.contains("png") || contentType.contains("gif");
            int imageType = supportsAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            BufferedImage resized = resizeAndConvert(original, imageType);

            String formatName = supportsAlpha ? "png" : "jpg";
            mimeType = supportsAlpha ? "image/png" : "image/jpeg";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, formatName, baos);
            imageBytes = baos.toByteArray();
        } else {
            // 解析不能なファイル形式はそのままバイナリ取得
            imageBytes = file.getBytes();
        }

        // バイナリを Base64 文字列（Data URI 形式）へ変換
        String base64Data = Base64.getEncoder().encodeToString(imageBytes);
        String dataUrl = "data:" + mimeType + ";base64," + base64Data;

        // DBに永続保存される Data URI 文字列を返却
        return Map.of("url", dataUrl);
    }

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
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, targetWidth, targetHeight);
        }
        g.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resized;
    }
}