package org.example.btl_java.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "accounts")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "account_name", length = 200, nullable = false)
    private String accountName;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "email", length = 200, unique = true, nullable = false)
    private String email;

    @Column(name = "role", length = 20)
    private String role;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;
}