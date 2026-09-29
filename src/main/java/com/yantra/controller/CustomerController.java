package com.yantra.controller;

import com.yantra.dto.CustomerDTO;
import com.yantra.dto.request.CustomerRequest;
import com.yantra.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Create new Customer")
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerRequest request) {
        CustomerDTO customer = customerService.createCustomer(request);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @Operation(summary = "Update Exiting Customer Data using Id")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody CustomerRequest request) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, request);
        return new ResponseEntity<>(customerDTO,HttpStatus.CREATED);
    }

    @Operation(summary = "Delete Exiting Customer")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Customer Deleted",HttpStatus.OK);
    }

    @Operation(summary = "Get all List Of Customer")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        List<CustomerDTO> allCustomers = customerService.getAllCustomers();
        return new ResponseEntity<>(allCustomers,HttpStatus.OK);
    }

    @Operation(summary = "Search Customer Using Phone Number")
    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> search(@RequestParam String phone) {
        List<CustomerDTO> customerDTOS = customerService.searchByPhone(phone);
        return new ResponseEntity<>(customerDTOS,HttpStatus.OK);
    }
}