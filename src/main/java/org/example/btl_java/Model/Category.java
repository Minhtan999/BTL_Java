package org.example.btl_java.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", length = 200, unique = true, nullable = false)
    private String categoryName;

    @Column(name = "description", length = 200)
    private String description;
}