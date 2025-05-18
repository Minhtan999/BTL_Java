package org.example.btl_java.Service;

import org.example.btl_java.DTO.CustomerDTO;
import org.example.btl_java.Model.Customer;
import org.example.btl_java.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Lấy tất cả khách hàng
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy khách hàng theo ID
    public CustomerDTO getCustomerById(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    // Tạo khách hàng mới
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    // Cập nhật khách hàng
    public CustomerDTO updateCustomer(Integer id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existingCustomer.setAccountId(customerDTO.getAccountId());
        existingCustomer.setFirstname(customerDTO.getFirstname());
        existingCustomer.setLastname(customerDTO.getLastname());
        existingCustomer.setPhone(customerDTO.getPhone());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setAddress(customerDTO.getAddress());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDTO(updatedCustomer);
    }

    // Xóa khách hàng
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    // Chuyển từ Entity sang DTO
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setAccountId(customer.getAccountId());
        dto.setFirstname(customer.getFirstname());
        dto.setLastname(customer.getLastname());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        return dto;
    }

    // Chuyển từ DTO sang Entity
    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setAccountId(dto.getAccountId());
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        return customer;
    }
}
