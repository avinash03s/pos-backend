package com.yantra.service;

import com.yantra.dto.ShiftReportDTO;

import java.util.List;

public interface ShiftReportService {
    ShiftReportDTO startShift();
    ShiftReportDTO endShift();
    List<ShiftReportDTO> getMyShifts();
    List<ShiftReportDTO> getAllShifts();
}