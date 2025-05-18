package org.example.btl_java.Controller;

import org.example.btl_java.DTO.ImageDTO;
import org.example.btl_java.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @Value("${upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File directory = new File(uploadPath);
        if (!directory.isAbsolute()) {
            uploadPath = new File(System.getProperty("user.dir"), uploadPath).getAbsolutePath();
        }
        logger.info("Upload Path initialized: {}", uploadPath);
    }

    // Lấy tất cả hình ảnh
    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    // Lấy hình ảnh theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Integer id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }

    // Lấy hình ảnh theo productId
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ImageDTO>> getImagesByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(imageService.getImagesByProductId(productId));
    }

    // Upload nhiều ảnh cho product
    @PostMapping("/upload-multiple")
    public ResponseEntity<?> uploadMultipleImages(@RequestParam("files") MultipartFile[] files,
                                                  @RequestParam("productId") Integer productId) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body("No files uploaded");
        }

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            try {
                Files.createDirectories(Paths.get(uploadPath));
            } catch (IOException e) {
                logger.error("Failed to create upload directory: {}", e.getMessage(), e);
                return ResponseEntity.status(500).body("Failed to create upload directory: " + e.getMessage());
            }
        }

        List<ImageDTO> uploadedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    String originalFileName = file.getOriginalFilename();
                    if (originalFileName == null) continue;

                    String fileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
                    File destinationFile = new File(uploadPath + File.separator + fileName);
                    file.transferTo(destinationFile);

                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setProductId(productId);
                    imageDTO.setImagePath(fileName);
                    uploadedImages.add(imageService.createImage(imageDTO));
                } catch (IOException e) {
                    logger.error("Error uploading file '{}': {}", file.getOriginalFilename(), e.getMessage(), e);
                    return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
                }
            }
        }

        return uploadedImages.isEmpty()
                ? ResponseEntity.badRequest().body("No valid files were uploaded")
                : ResponseEntity.ok(uploadedImages);
    }

    // Upload một ảnh duy nhất
    @PostMapping("/upload-single")
    public ResponseEntity<?> uploadSingleImage(@RequestParam("file") MultipartFile file,
                                               @RequestParam("productId") Integer productId) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            try {
                Files.createDirectories(Paths.get(uploadPath));
            } catch (IOException e) {
                logger.error("Failed to create upload directory: {}", e.getMessage(), e);
                return ResponseEntity.status(500).body("Failed to create upload directory: " + e.getMessage());
            }
        }

        try {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                return ResponseEntity.badRequest().body("Invalid file name");
            }

            String fileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
            File destinationFile = new File(uploadPath + File.separator + fileName);
            file.transferTo(destinationFile);

            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setProductId(productId);
            imageDTO.setImagePath(fileName);
            ImageDTO savedImage = imageService.createImage(imageDTO);

            logger.info("Uploaded single file: {}", fileName);
            return ResponseEntity.ok(savedImage);
        } catch (IOException e) {
            logger.error("Error uploading file '{}': {}", file.getOriginalFilename(), e.getMessage(), e);
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

    // Lấy file ảnh
    @GetMapping("/file/{imageName}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable String imageName) {
        try {
            // Normalize imageName by removing leading slashes
            String normalizedImageName = imageName.replaceFirst("^/+", "");
            File file = new File(uploadPath + File.separator + normalizedImageName);
            if (!file.exists()) {
                logger.warn("Image not found: {}", normalizedImageName);
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (IOException e) {
            logger.error("Error reading image '{}': {}", imageName, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    // Xóa hình ảnh
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}