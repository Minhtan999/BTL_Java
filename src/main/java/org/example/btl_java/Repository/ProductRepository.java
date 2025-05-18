package org.example.btl_java.Repository;

import org.example.btl_java.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
}
