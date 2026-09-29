package com.yantra.service;

import com.yantra.dto.CustomerDTO;
import com.yantra.dto.request.CustomerRequest;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerRequest request);
    CustomerDTO updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO> searchByPhone(String phone);
}