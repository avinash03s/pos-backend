package com.yantra.controller;

import com.yantra.dto.RefundDTO;
import com.yantra.dto.request.RefundRequest;
import com.yantra.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundDTO> createRefund(@RequestBody RefundRequest request) {
        RefundDTO refund = refundService.createRefund(request);
        return new ResponseEntity<>(refund, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RefundDTO>> getAllRefunds() {
        List<RefundDTO> allRefunds = refundService.getAllRefunds();
        return new ResponseEntity<>(allRefunds,HttpStatus.OK);
    }
}