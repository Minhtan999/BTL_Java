package org.example.btl_java.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "suppliers")
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_name", length = 200, nullable = false)
    private String supplierName;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "phone", length = 20, unique = true)
    private String phone;

    @Column(name = "email", length = 50, unique = true)
    private String email;
}