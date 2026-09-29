package com.yantra.service;

import com.yantra.dto.BranchDTO;
import com.yantra.dto.request.BranchRequest;

import java.util.List;

public interface BranchService {
    BranchDTO createBranch(BranchRequest request);
    BranchDTO updateBranch(Long id, BranchRequest request);
    void deactivateBranch(Long id);
    List<BranchDTO> getAllBranches();
    BranchDTO getBranchById(Long id);
    void activateBranch(Long id);
}