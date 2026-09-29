package com.yantra.service.serviceImp;

import com.yantra.constant.Roles;
import com.yantra.dto.request.AddEmployeeRequest;
import com.yantra.dto.response.EmployeeResponse;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.repositroy.UserRepository;
import com.yantra.service.EmployeeService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public EmployeeResponse addEmployee(AddEmployeeRequest request) {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Roles.ROLE_ADMIN
                && currentUser.getRole() != Roles.ROLE_STORE_MANAGER) {
            throw new PosException("EMPLOYEE_ADD_FORBIDDEN");
        }

        if (request.role() == Roles.ROLE_ADMIN) {
            throw new PosException("EMPLOYEE_ADMIN_NOT_ALLOWED");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new PosException("EMAIL_ALREADY_REGISTERED");
        }

        User employee = new User();
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setPhone(request.phone());
        employee.setPassword(passwordEncoder.encode(request.password()));
        employee.setRole(request.role());
        employee.setTenantId(currentUser.getTenantId());
        employee.setBranchId(request.branchId());
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(employee);
        return toResponse(saved);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        User currentUser = userService.getCurrentUser();
        return userRepository.findAllByTenantId(currentUser.getTenantId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByBranch(Long branchId) {
        User currentUser = userService.getCurrentUser();
        return userRepository.findAllByTenantIdAndBranchId(currentUser.getTenantId(), branchId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateEmployee(Long employeeId) {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Roles.ROLE_ADMIN
                && currentUser.getRole() != Roles.ROLE_STORE_MANAGER) {
            throw new PosException("EMPLOYEE_DEACTIVATE_FORBIDDEN");
        }

        User employee = userRepository.findByIdAndTenantId(employeeId, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("EMPLOYEE_NOT_FOUND"));

        if (employee.getRole() == Roles.ROLE_ADMIN) {
            throw new PosException("EMPLOYEE_CANNOT_DEACTIVATE_ADMIN");
        }

        employee.setActive(false);
        employee.setUpdatedAt(LocalDateTime.now());
        userRepository.save(employee);
    }

    private EmployeeResponse toResponse(User u) {
        return new EmployeeResponse(u.getId(), u.getName(), u.getEmail(), u.getPhone(),
                u.getRole(), u.getTenantId(), u.getBranchId(), u.isActive(),
                u.getCreatedAt(), u.getUpdatedAt(), u.getLastLoginAt());
    }
}