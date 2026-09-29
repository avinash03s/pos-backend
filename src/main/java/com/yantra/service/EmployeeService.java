package com.yantra.service;

import com.yantra.dto.request.AddEmployeeRequest;
import com.yantra.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse addEmployee(AddEmployeeRequest request);
    List<EmployeeResponse> getAllEmployees();
    List<EmployeeResponse> getEmployeesByBranch(Long branchId);
    void deactivateEmployee(Long employeeId);
}