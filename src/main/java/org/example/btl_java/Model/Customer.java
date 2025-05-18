package org.example.btl_java.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "account_id", unique = true)
    private Integer accountId;

    @Column(name = "firstname", length = 200, nullable = false)
    private String firstname;

    @Column(name = "lastname", length = 200, nullable = false)
    private String lastname;

    @Column(name = "phone", length = 20, unique = true)
    private String phone;

    @Column(name = "email", length = 50, unique = true, nullable = false)
    private String email;

    @Column(name = "address", length = 200)
    private String address;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;
}
