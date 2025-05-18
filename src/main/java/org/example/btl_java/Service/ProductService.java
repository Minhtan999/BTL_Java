package org.example.btl_java.Service;

import org.example.btl_java.DTO.ProductDTO;
import org.example.btl_java.Model.Product;
import org.example.btl_java.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy sản phẩm theo ID
    public ProductDTO getProductById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Tạo sản phẩm mới
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    // Cập nhật sản phẩm
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setCategoryId(productDTO.getCategoryId());
        existingProduct.setSupplierId(productDTO.getSupplierId());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    // Xóa sản phẩm
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    // Chuyển từ Entity sang DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategoryId());
        dto.setSupplierId(product.getSupplierId());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getQuantity());
        return dto;
    }

    // Chuyển từ DTO sang Entity
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setCategoryId(dto.getCategoryId());
        product.setSupplierId(dto.getSupplierId());
        product.setDescription(dto.getDescription());
        product.setQuantity(dto.getQuantity());
        return product;
    }
}