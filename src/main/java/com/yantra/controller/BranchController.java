package com.yantra.controller;

import com.yantra.dto.BranchDTO;
import com.yantra.dto.request.BranchRequest;
import com.yantra.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @Operation(summary = "Create New Branch")
    @PostMapping
    public ResponseEntity<BranchDTO> create(@RequestBody BranchRequest request) {
        BranchDTO branch = branchService.createBranch(request);
        return new ResponseEntity<>(branch, HttpStatus.CREATED);
    }

    @Operation(summary = "Update Exiting Branch")
    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> update(@PathVariable Long id, @RequestBody BranchRequest request) {
        BranchDTO branchDTO = branchService.updateBranch(id, request);
        return new ResponseEntity<>(branchDTO,HttpStatus.OK);
    }

    @Operation(summary = "Update Specific filed")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        branchService.deactivateBranch(id);
        return new ResponseEntity<>("Branch Deactivate",HttpStatus.OK);
    }

    @Operation(summary = "Get All Branches")
    @GetMapping
    public ResponseEntity<List<BranchDTO>> getAll() {
        List<BranchDTO> allBranches = branchService.getAllBranches();
        return new ResponseEntity<>(allBranches,HttpStatus.OK);
    }


    @PatchMapping("/{id}/activate")
    public ResponseEntity<String> activate(@PathVariable Long id) {
        branchService.activateBranch(id);
        return new ResponseEntity<>("Branch Activated", HttpStatus.OK);
    }
}