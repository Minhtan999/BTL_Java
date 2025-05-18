package org.example.btl_java.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price; // Thay Double bằng BigDecimal

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "quantity")
    private Integer quantity;
}