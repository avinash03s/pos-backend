package com.yantra.service;

import com.yantra.dto.RefundDTO;
import com.yantra.dto.request.RefundRequest;

import java.util.List;

public interface RefundService {
    RefundDTO createRefund(RefundRequest request);
    List<RefundDTO> getAllRefunds();
}