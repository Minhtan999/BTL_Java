package org.example.btl_java.Repository;

import org.example.btl_java.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByProductId(Integer productId);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.productId = :productId")
    void deleteAllByProductId(Integer productId);
}