package com.yantra.controller;

import com.yantra.dto.ShiftReportDTO;
import com.yantra.service.ShiftReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftReportController {

    private final ShiftReportService shiftReportService;

    @PostMapping("/start")
    public ResponseEntity<ShiftReportDTO> startShift() {
        ShiftReportDTO shiftReportDTO = shiftReportService.startShift();
        return new ResponseEntity<>(shiftReportDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/end")
    public ResponseEntity<ShiftReportDTO> endShift() {
        ShiftReportDTO shiftReportDTO = shiftReportService.endShift();
        return new ResponseEntity<>(shiftReportDTO,HttpStatus.CREATED);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ShiftReportDTO>> getMyShifts() {
        List<ShiftReportDTO> myShifts = shiftReportService.getMyShifts();
        return new ResponseEntity<>(myShifts,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ShiftReportDTO>> getAllShifts() {
        List<ShiftReportDTO> allShifts = shiftReportService.getAllShifts();
        return new ResponseEntity<>(allShifts,HttpStatus.OK);
    }
}