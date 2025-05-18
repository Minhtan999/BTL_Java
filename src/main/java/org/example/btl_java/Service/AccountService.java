package org.example.btl_java.Service;

import org.example.btl_java.DTO.AccountDTO;
import org.example.btl_java.Model.Account;
import org.example.btl_java.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Lấy tất cả tài khoản
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy tài khoản theo ID
    public AccountDTO getAccountById(Integer id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    // Tạo tài khoản mới
    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = convertToEntity(accountDTO);
        Account savedAccount = accountRepository.save(account);
        return convertToDTO(savedAccount);
    }

    // Cập nhật tài khoản
    public AccountDTO updateAccount(Integer id, AccountDTO accountDTO) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        existingAccount.setAccountName(accountDTO.getAccountName());
        existingAccount.setPassword(accountDTO.getPassword());
        existingAccount.setEmail(accountDTO.getEmail());
        existingAccount.setRole(accountDTO.getRole());

        Account updatedAccount = accountRepository.save(existingAccount);
        return convertToDTO(updatedAccount);
    }

    // Xóa tài khoản
    public void deleteAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        accountRepository.delete(account);
    }

    // Chuyển từ Entity sang DTO
    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        dto.setAccountName(account.getAccountName());
        dto.setPassword(account.getPassword());
        dto.setEmail(account.getEmail());
        dto.setRole(account.getRole());
        dto.setCreatedAt(account.getCreatedAt());
        return dto;
    }

    // Chuyển từ DTO sang Entity
    private Account convertToEntity(AccountDTO dto) {
        Account account = new Account();
        account.setAccountId(dto.getAccountId());
        account.setAccountName(dto.getAccountName());
        account.setPassword(dto.getPassword());
        account.setEmail(dto.getEmail());
        account.setRole(dto.getRole());
        account.setCreatedAt(dto.getCreatedAt());
        return account;
    }
}
