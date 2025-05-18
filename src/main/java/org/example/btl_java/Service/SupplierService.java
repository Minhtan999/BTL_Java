package org.example.btl_java.Service;

import org.example.btl_java.DTO.SupplierDTO;
import org.example.btl_java.Model.Supplier;
import org.example.btl_java.Repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // Lấy tất cả nhà cung cấp
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy nhà cung cấp theo ID
    public SupplierDTO getSupplierById(Integer id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        return supplier.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    // Tạo nhà cung cấp mới
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = convertToEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDTO(savedSupplier);
    }

    // Cập nhật nhà cung cấp
    public SupplierDTO updateSupplier(Integer id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        existingSupplier.setSupplierName(supplierDTO.getSupplierName());
        existingSupplier.setAddress(supplierDTO.getAddress());
        existingSupplier.setPhone(supplierDTO.getPhone());
        existingSupplier.setEmail(supplierDTO.getEmail());

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return convertToDTO(updatedSupplier);
    }

    // Xóa nhà cung cấp
    public void deleteSupplier(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        supplierRepository.delete(supplier);
    }

    // Chuyển từ Entity sang DTO
    private SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setSupplierId(supplier.getSupplierId());
        dto.setSupplierName(supplier.getSupplierName());
        dto.setAddress(supplier.getAddress());
        dto.setPhone(supplier.getPhone());
        dto.setEmail(supplier.getEmail());
        return dto;
    }

    // Chuyển từ DTO sang Entity
    private Supplier convertToEntity(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(dto.getSupplierId());
        supplier.setSupplierName(dto.getSupplierName());
        supplier.setAddress(dto.getAddress());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        return supplier;
    }
}
