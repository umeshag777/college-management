package com.example.collegemanagement.controller;

import com.example.collegemanagement.dto.CollegeDto;
import com.example.collegemanagement.dto.CollegeInformationDto;
import com.example.collegemanagement.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @PostMapping("/add")
    public ResponseEntity<String> addCollege(@RequestBody CollegeInformationDto collegeInformationDTO) {
        try {
            collegeService.addCollegeInformation(collegeInformationDTO);
            return ResponseEntity.status(HttpStatus.OK).body("College information added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add college information: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollegeById(@PathVariable Long id) {
        try {
            CollegeDto collegeDTO = collegeService.getCollegeById(id);
            if (collegeDTO != null) {
                return ResponseEntity.ok(collegeDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No college found with the id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCollege(@PathVariable Long id, @RequestBody CollegeDto collegeDTO) {
        try {
            CollegeDto collegeDto = collegeService.updateCollege(id, collegeDTO);
            if(collegeDto != null) {
                return ResponseEntity.ok("College information updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("College information not updated successfully for the id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update college information: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCollege(@PathVariable Long id) {
        try {
            collegeService.deleteCollege(id);
            return ResponseEntity.ok("College information deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete college information: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllColleges() {
        try {
            List<CollegeDto> colleges = collegeService.getAllColleges();
            if (!colleges.isEmpty()) {
                return ResponseEntity.ok(colleges);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Colleges Found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
