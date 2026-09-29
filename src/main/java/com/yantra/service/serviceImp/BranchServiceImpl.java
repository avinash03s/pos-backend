package com.yantra.service.serviceImp;

import com.yantra.constant.Roles;
import com.yantra.dto.BranchDTO;
import com.yantra.dto.request.BranchRequest;
import com.yantra.entity.Branch;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.repositroy.BranchRepository;
import com.yantra.service.BranchService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final UserService userService;

    @Override
    public BranchDTO createBranch(BranchRequest request) {
        User currentUser = userService.getCurrentUser();

        // Only Admin Create New Branch
        if (currentUser.getRole() != Roles.ROLE_ADMIN) {
            throw new PosException("BRANCH_CREATE_FORBIDDEN");
        }

        if (request.name() == null || request.name().isBlank()) {
            throw new PosException("BRANCH_NAME_REQUIRED");
        }

        if (request.branchCode() != null && !request.branchCode().isBlank()
                && branchRepository.existsByBranchCodeAndTenantId(request.branchCode(), currentUser.getTenantId())) {
            throw new PosException("BRANCH_CODE_ALREADY_EXISTS");
        }

        Branch branch = new Branch();
        branch.setTenantId(currentUser.getTenantId());
        branch.setName(request.name());
        branch.setAddress(request.address());
        branch.setPhone(request.phone());
        branch.setEmail(request.email());
        branch.setBranchCode(request.branchCode());
        branch.setCity(request.city());
        branch.setState(request.state());
        branch.setPincode(request.pincode());
        branch.setGstNumber(request.gstNumber());
        branch.setCreatedAt(LocalDateTime.now());
        branch.setUpdatedAt(LocalDateTime.now());

        return branchRepository.save(branch).toDTO();
    }

    @Override
    public BranchDTO updateBranch(Long id, BranchRequest request) {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Roles.ROLE_ADMIN) {
            throw new PosException("BRANCH_UPDATE_FORBIDDEN");
        }

        Branch branch = branchRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("BRANCH_NOT_FOUND"));

        // If branchCode is changing, make sure the new one isn't already taken
        if (request.branchCode() != null && !request.branchCode().isBlank()
                && !request.branchCode().equals(branch.getBranchCode())
                && branchRepository.existsByBranchCodeAndTenantId(request.branchCode(), currentUser.getTenantId())) {
            throw new PosException("BRANCH_CODE_ALREADY_EXISTS");
        }

        branch.setName(request.name());
        branch.setAddress(request.address());
        branch.setPhone(request.phone());
        branch.setEmail(request.email());
        branch.setBranchCode(request.branchCode());
        branch.setCity(request.city());
        branch.setState(request.state());
        branch.setPincode(request.pincode());
        branch.setGstNumber(request.gstNumber());
        branch.setUpdatedAt(LocalDateTime.now());

        return branchRepository.save(branch).toDTO();
    }

    @Override
    public void deactivateBranch(Long id) {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Roles.ROLE_ADMIN) {
            throw new PosException("BRANCH_DEACTIVE_FORBIDDEN");
        }

        Branch branch = branchRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("BRANCH_NOT_FOUND"));

        branch.setActive(false);
        branch.setUpdatedAt(LocalDateTime.now());
        branchRepository.save(branch);
    }

    @Override
    public List<BranchDTO> getAllBranches() {
        User currentUser = userService.getCurrentUser();
        return branchRepository.findAllByTenantId(currentUser.getTenantId())
                .stream().map(Branch::toDTO).collect(Collectors.toList());
    }

    @Override
    public BranchDTO getBranchById(Long id) {
        User currentUser = userService.getCurrentUser();
        Branch branch = branchRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("BRANCH_NOT_FOUND"));
        return branch.toDTO();
    }

    @Override
    public void activateBranch(Long id) {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Roles.ROLE_ADMIN) {
            throw new PosException("BRANCH_ACTIVATE_FORBIDDEN");
        }

        Branch branch = branchRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("BRANCH_NOT_FOUND"));

        branch.setActive(true);
        branch.setUpdatedAt(LocalDateTime.now());
        branchRepository.save(branch);
    }
}