package com.rikkei.microservice.pharmacyservice.controller;

import com.rikkei.microservice.pharmacyservice.entity.Medicine;
import com.rikkei.microservice.pharmacyservice.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final MedicineRepository medicineRepository;

    @Value("${app.branch-name:Unknown Branch}")
    private String branchName;

    @Value("${app.hotline:1900xxxx}")
    private String hotline;

    @Value("${app.address:N/A}")
    private String address;

    @Value("${spring.datasource.url:Unknown}")
    private String datasourceUrl;

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getBranchInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("branchName", branchName);
        info.put("hotline", hotline);
        info.put("address", address);
        info.put("datasourceUrl", datasourceUrl);
        return ResponseEntity.ok(info);
    }

    @GetMapping("/medicines")
    public ResponseEntity<List<Medicine>> getAllMedicines() {
        return ResponseEntity.ok(medicineRepository.findAll());
    }

    @PostMapping("/medicines")
    public ResponseEntity<Medicine> createMedicine(@RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineRepository.save(medicine));
    }
}
