package com.yantra.controller;

import com.yantra.dto.request.AddEmployeeRequest;
import com.yantra.dto.response.EmployeeResponse;
import com.yantra.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create new Employee by Business Owner(Admin)")
    @PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody AddEmployeeRequest request) {
        EmployeeResponse response = employeeService.addEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
        return new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByBranch(@PathVariable Long branchId) {
        List<EmployeeResponse> employees = employeeService.getEmployeesByBranch(branchId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateEmployee(@PathVariable Long id) {
        employeeService.deactivateEmployee(id);
        return new ResponseEntity<>("Employee Deactivated", HttpStatus.OK);
    }
}