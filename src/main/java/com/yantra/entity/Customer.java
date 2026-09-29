package com.yantra.entity;

import com.yantra.dto.CustomerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer", indexes = {
        @Index(name = "idx_customer_tenant", columnList = "tenantId")
})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String name;

    private String phone;

    private String email;

    private LocalDateTime createdAt;

    public CustomerDTO toDTO() {
        return new CustomerDTO(this.id, this.tenantId, this.name, this.phone, this.email, this.createdAt);
    }
}