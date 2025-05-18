package org.example.btl_java.Repository;

import org.example.btl_java.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    void deleteByOrderId(Integer orderId);
}