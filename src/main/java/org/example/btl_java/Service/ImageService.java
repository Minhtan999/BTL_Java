package org.example.btl_java.Service;

import org.example.btl_java.DTO.ImageDTO;
import org.example.btl_java.Model.Image;
import org.example.btl_java.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public List<ImageDTO> getAllImages() {
        return imageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ImageDTO getImageById(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return convertToDTO(image);
    }

    public List<ImageDTO> getImagesByProductId(Integer productId) {
        return imageRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ImageDTO createImage(ImageDTO imageDTO) {
        Image image = new Image();
        image.setProductId(imageDTO.getProductId());
        image.setImagePath(imageDTO.getImagePath());
        image = imageRepository.save(image);
        return convertToDTO(image);
    }

    public ImageDTO updateImage(Integer id, ImageDTO imageDTO) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        image.setProductId(imageDTO.getProductId());
        image.setImagePath(imageDTO.getImagePath());
        image = imageRepository.save(image);
        return convertToDTO(image);
    }

    public void deleteImage(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        String imagePath = image.getImagePath();
        imageRepository.deleteById(id);

        // Xóa tệp vật lý
        File file = new File(uploadPath + File.separator + imagePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("Failed to delete image file: " + imagePath);
            }
        }
    }

    private ImageDTO convertToDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImageId(image.getImageId());
        imageDTO.setProductId(image.getProductId());
        String normalizedPath = image.getImagePath();
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }
        imageDTO.setImagePath(normalizedPath);
        imageDTO.setUploadedAt(image.getUploadedAt());
        return imageDTO;
    }
}