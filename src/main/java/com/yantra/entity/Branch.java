package com.yantra.entity;

import com.yantra.dto.BranchDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branch", indexes = {
        @Index(name = "idx_branch_tenant", columnList = "tenantId")
})
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String name;        // e.g. "Beed Branch"

    private String address;

    private String phone;

    private String email;

    private String branchCode;  // e.g. "BEED-01"

    private String city;

    private String state;

    private String pincode;

    private String gstNumber;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public BranchDTO toDTO() {
        return new BranchDTO(this.id, this.tenantId, this.name, this.address, this.phone,
                this.email, this.branchCode, this.city, this.state, this.pincode,
                this.gstNumber, this.active, this.createdAt, this.updatedAt);
    }
}