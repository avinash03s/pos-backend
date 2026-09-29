package com.yantra.service.serviceImp;

import com.yantra.dto.CustomerDTO;
import com.yantra.dto.request.CustomerRequest;
import com.yantra.entity.Customer;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.repositroy.CustomerRepository;
import com.yantra.service.CustomerService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Override
    public CustomerDTO createCustomer(CustomerRequest request) {
        User currentUser = userService.getCurrentUser();

        if (request.name() == null || request.name().isBlank()) {
            throw new PosException("CUSTOMER_NAME_REQUIRED");
        }
        if (request.phone() == null || request.phone().isBlank()) {
            throw new PosException("CUSTOMER_PHONE_REQUIRED");
        }

        Customer customer = new Customer();
        customer.setTenantId(currentUser.getTenantId());
        customer.setName(request.name());
        customer.setPhone(request.phone());
        customer.setEmail(request.email());
        customer.setCreatedAt(LocalDateTime.now());

        return customerRepository.save(customer).toDTO();
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerRequest request) {
        User currentUser = userService.getCurrentUser();

        Customer customer = customerRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("CUSTOMER_NOT_FOUND"));

        customer.setName(request.name());
        customer.setPhone(request.phone());
        customer.setEmail(request.email());

        return customerRepository.save(customer).toDTO();
    }

    @Override
    public void deleteCustomer(Long id) {
        User currentUser = userService.getCurrentUser();

        Customer customer = customerRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("CUSTOMER_NOT_FOUND"));

        customerRepository.delete(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        User currentUser = userService.getCurrentUser();
        return customerRepository.findAllByTenantId(currentUser.getTenantId())
                .stream().map(Customer::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchByPhone(String phone) {
        User currentUser = userService.getCurrentUser();
        return customerRepository.findAllByTenantIdAndPhoneContaining(currentUser.getTenantId(), phone)
                .stream().map(Customer::toDTO).collect(Collectors.toList());
    }
}