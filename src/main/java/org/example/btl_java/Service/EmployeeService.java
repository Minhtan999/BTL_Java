package org.example.btl_java.Service;

import org.example.btl_java.DTO.EmployeeDTO;
import org.example.btl_java.Model.Employee;
import org.example.btl_java.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Lấy tất cả nhân viên
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy nhân viên theo ID
    public EmployeeDTO getEmployeeById(Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    // Tạo nhân viên mới
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    // Cập nhật nhân viên
    public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        existingEmployee.setAccountId(employeeDTO.getAccountId());
        existingEmployee.setFirstname(employeeDTO.getFirstname());
        existingEmployee.setLastname(employeeDTO.getLastname());
        existingEmployee.setPhone(employeeDTO.getPhone());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setAddress(employeeDTO.getAddress());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return convertToDTO(updatedEmployee);
    }

    // Xóa nhân viên
    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    // Chuyển từ Entity sang DTO
    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setAccountId(employee.getAccountId());
        dto.setFirstname(employee.getFirstname());
        dto.setLastname(employee.getLastname());
        dto.setPhone(employee.getPhone());
        dto.setEmail(employee.getEmail());
        dto.setAddress(employee.getAddress());
        return dto;
    }

    // Chuyển từ DTO sang Entity
    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setAccountId(dto.getAccountId());
        employee.setFirstname(dto.getFirstname());
        employee.setLastname(dto.getLastname());
        employee.setPhone(dto.getPhone());
        employee.setEmail(dto.getEmail());
        employee.setAddress(dto.getAddress());
        return employee;
    }
}