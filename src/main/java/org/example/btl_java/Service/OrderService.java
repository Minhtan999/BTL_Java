package org.example.btl_java.Service;

import org.example.btl_java.DTO.OrderDTO;
import org.example.btl_java.DTO.OrderItemDTO;
import org.example.btl_java.Model.Order;
import org.example.btl_java.Model.OrderItem;
import org.example.btl_java.Repository.OrderItemRepository;
import org.example.btl_java.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Lấy tất cả đơn hàng
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy đơn hàng theo ID
    public OrderDTO getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    // Tạo đơn hàng mới
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        order.setOrderDate(LocalDateTime.now());

        // Tính total_amount từ orderItems
        BigDecimal totalAmount = calculateTotalAmount(orderDTO.getOrderItems());
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Lưu order_items
        if (orderDTO.getOrderItems() != null) {
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                OrderItem item = convertToOrderItemEntity(itemDTO);
                item.setOrderId(savedOrder.getOrderId());
                orderItemRepository.save(item);
            }
        }

        return convertToDTO(savedOrder);
    }

    // Cập nhật đơn hàng
    @Transactional
    public OrderDTO updateOrder(Integer id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingOrder.setCustomerId(orderDTO.getCustomerId());

        // Xóa các order_items cũ
        orderItemRepository.deleteByOrderId(id);

        // Cập nhật order_items mới
        if (orderDTO.getOrderItems() != null) {
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                OrderItem item = convertToOrderItemEntity(itemDTO);
                item.setOrderId(id);
                orderItemRepository.save(item);
            }
        }

        // Cập nhật total_amount
        BigDecimal totalAmount = calculateTotalAmount(orderDTO.getOrderItems());
        existingOrder.setTotalAmount(totalAmount);

        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDTO(updatedOrder);
    }

    // Xóa đơn hàng
    @Transactional
    public void deleteOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        // order_items sẽ tự động xóa do ON DELETE CASCADE
        orderRepository.delete(order);
    }

    // Tính total_amount từ orderItems
    private BigDecimal calculateTotalAmount(List<OrderItemDTO> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Chuyển từ Entity sang DTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());

        // Lấy order_items
        List<OrderItem> items = orderItemRepository.findAll().stream()
                .filter(item -> item.getOrderId().equals(order.getOrderId()))
                .collect(Collectors.toList());
        dto.setOrderItems(items.stream().map(this::convertToOrderItemDTO).collect(Collectors.toList()));

        return dto;
    }

    // Chuyển từ DTO sang Entity
    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setCustomerId(dto.getCustomerId());
        order.setOrderDate(dto.getOrderDate());
        order.setTotalAmount(dto.getTotalAmount());
        return order;
    }

    // Chuyển OrderItem Entity sang DTO
    private OrderItemDTO convertToOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setOrderId(item.getOrderId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }

    // Chuyển OrderItem DTO sang Entity
    private OrderItem convertToOrderItemEntity(OrderItemDTO dto) {
        OrderItem item = new OrderItem();
        item.setOrderItemId(dto.getOrderItemId());
        item.setOrderId(dto.getOrderId());
        item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        return item;
    }
}